package org.academo.academo.e2e;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.flyway.enabled=false",
                "spring.sql.init.mode=never",
                "spring.test.database.replace=NONE",
                "spring.datasource.driver-class-name=org.postgresql.Driver",

                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",

                "logging.level.org.mongodb.driver=ERROR"
        }
)
@Tag("e2e")
@Sql(
        scripts = {
                "classpath:db-test/schema_e2e.sql",
                "classpath:db-test/e2e_seed.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class MvpE2EITCase extends PostgresITBase {

    @LocalServerPort int port;

    @BeforeEach
    void setup() throws Exception {
        // base URI and port: allow override via env or system properties for CI
        // Priority: system property -> env var -> default (localhost + @LocalServerPort)
        String baseUri = System.getProperty("e2e.base.uri", System.getenv().getOrDefault("E2E_BASE_URI", "http://localhost"));
        String basePortProp = System.getProperty("e2e.base.port", System.getenv().get("E2E_BASE_PORT"));

        RestAssured.baseURI = baseUri;

        if (basePortProp != null && !basePortProp.isBlank()) {
            try {
                RestAssured.port = Integer.parseInt(basePortProp);
            } catch (NumberFormatException ex) {
                // fallback to @LocalServerPort
                RestAssured.port = port;
            }
        } else {
            // default: use the Spring injected random port
            RestAssured.port = port;
        }

        // Proxy config: enable only when explicitly provided via system prop / env var
        // Example: -Dmitm.proxy.host=127.0.0.1 -Dmitm.proxy.port=8082  OR export MITM_PROXY_HOST, MITM_PROXY_PORT
        String proxyHost = System.getProperty("mitm.proxy.host", System.getenv().get("MITM_PROXY_HOST"));
        String proxyPort = System.getProperty("mitm.proxy.port", System.getenv().get("MITM_PROXY_PORT"));

        if (proxyHost != null && !proxyHost.isBlank() && proxyPort != null && !proxyPort.isBlank()) {
            try {
                int p = Integer.parseInt(proxyPort);
                RestAssured.proxy(proxyHost, p);
                RestAssured.useRelaxedHTTPSValidation(); // allow mitmproxy self-signed certs
                System.out.printf("E2E: using proxy %s:%d%n", proxyHost, p);
            } catch (NumberFormatException ignore) {
                System.out.println("E2E: invalid proxy port, skipping proxy setup");
            }
        } else {
            // No proxy configured - keep original behavior
            System.out.println("E2E: no proxy configured, direct requests");
        }

        PrintStream log = new PrintStream("target/e2e-http.log");
        RestAssured.replaceFiltersWith(new RequestLoggingFilter(log), new ResponseLoggingFilter(log));
    }


    // =================== Scenario 1: Admin creates Teacher & Student; Teacher creates a Task ===================
    @Test
    void teacher_creates_task() {
        // Create users
        createUser("Alice Teacher", "teacher_alice", "P@ss", "TEACHER");
        createUser("Bob Student", "student_bob", "P@ss", "STUDENT");

        // Fetch their UUIDs
        UUID teacherId = getUserIdByUsername("teacher_alice");
        UUID studentId = getUserIdByUsername("student_bob");

        // Teacher creates a task assigned to student
        given()
                .contentType("application/json")
                .body(Map.of(
                        "title", "Lab 2",
                        "description", "Integration + E2E",
                        "teacherId", teacherId.toString(),
                        "studentId", studentId.toString()
                ))
                .when()
                .post("/task/new")
                .then()
                .statusCode(201);

        // Verify via list
        given()
                .when()
                .get("/task/all")
                .then()
                .statusCode(200)
                .body("title", hasItem("Lab 2"));
    }

    // =================== Scenario 2: Student submits an Answer for a Task ===================
    @Test
    void student_submits_task() {
        // Arrange: create users + task
        createUser("Alice Teacher", "teacher_alice", "P@ss", "TEACHER");
        createUser("Bob Student", "student_bob", "P@ss", "STUDENT");
        UUID teacherId = getUserIdByUsername("teacher_alice");
        UUID studentId = getUserIdByUsername("student_bob");
        UUID taskId = createTask("HW 1", "Solve it", teacherId, studentId);

        // Act: student submits
        UUID submissionId = extractSubmissionId(
                given()
                        .contentType("application/json")
                        .body(Map.of(
                                "teacherId", teacherId.toString(),
                                "studentId", studentId.toString(),
                                "taskId", taskId.toString(),
                                "answer", "My solution text..."
                        ))
                        .when()
                        .post("/submit/new")
                        .then()
                        .statusCode(201)
        );

        // Assert
        given()
                .queryParam("id", submissionId.toString())
                .when()
                .get("/submit/one")
                .then()
                .statusCode(200)
                .body("taskId", equalTo(taskId.toString()))
                .body("studentId", equalTo(studentId.toString()))
                .body("answer", equalTo("My solution text..."));
    }

    // =================== Scenario 3: Teacher grades a Submission ===================
    @Test
    void teacher_grades_submission() {
        // Arrange
        createUser("Alice Teacher", "teacher_alice", "P@ss", "TEACHER");
        createUser("Bob Student", "student_bob", "P@ss", "STUDENT");
        UUID teacherId = getUserIdByUsername("teacher_alice");
        UUID studentId = getUserIdByUsername("student_bob");
        UUID taskId = createTask("HW 2", "Do it", teacherId, studentId);

        UUID submissionId = extractSubmissionId(
                given()
                        .contentType("application/json")
                        .body(Map.of(
                                "teacherId", teacherId.toString(),
                                "studentId", studentId.toString(),
                                "taskId", taskId.toString(),
                                "answer", "Answer..."
                        ))
                        .when()
                        .post("/submit/new")
                        .then()
                        .statusCode(201)
        );

        // Act: grade it
        given()
                .contentType("application/json")
                .body(Map.of(
                        "value", 9.5,
                        "feedback", "Well done",
                        "submissionId", submissionId.toString()
                ))
                .when()
                .post("/grade/new")
                .then()
                .statusCode(201);

        // Assert
        given()
                .queryParam("id", submissionId.toString())
                .when()
                .get("/grade/one")
                .then()
                .statusCode(200)
                .body("submissionId", equalTo(submissionId.toString()))
                .body("value.toString()", equalTo("9.5")) // tolerant to float/double
                .body("feedback", equalTo("Well done"));
    }

    // =================== Scenario 4: FAQ — Student asks, Teacher answers, Student reads the answer ===================
    @Test
    void faq_student_asks_teacher_answers() {
        // Create teacher & student
        createUser("Alice Teacher", "teacher_alice", "P@ss", "TEACHER");
        createUser("Bob Student", "student_bob", "P@ss", "STUDENT");
        UUID teacherId = getUserIdByUsername("teacher_alice");
        UUID studentId = getUserIdByUsername("student_bob");

        // Student asks a question (DTO uses "studentID")
        String qText = "When is the deadline for HW 3?";
        UUID questionId = extractQuestionIdFromAll(
                given()
                        .contentType("application/json")
                        .body(Map.of(
                                "studentID", studentId.toString(),
                                "question", qText
                        ))
                        .when()
                        .post("/FAQ/new")
                        .then()
                        .statusCode(201),
                qText
        );

        // Teacher answers (DTO uses "questionID" and "teacherID")
        Map<String, Object> answerPayload = new HashMap<>();
        answerPayload.put("questionID", questionId.toString());
        answerPayload.put("teacherID", teacherId.toString());
        answerPayload.put("answer", "Deadline is Friday 23:59.");

        given()
                .contentType("application/json")
                .body(answerPayload)
                .when()
                .post("/answer/new")
                .then()
                .statusCode(201);

        // Fetch the answer (controller expects "questionID")
        // Retry briefly in case of tiny lag
        io.restassured.response.Response finalResp =
                given().queryParam("questionID", questionId.toString())
                        .when().get("/answer/for");
        for (int i = 0; i < 5 && finalResp.statusCode() == 404; i++) {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            finalResp = given().queryParam("questionID", questionId.toString())
                    .when().get("/answer/for");
        }

        finalResp.then()
                .statusCode(200)
                .body("questionID", equalTo(questionId.toString()))
                .body("answer", equalTo("Deadline is Friday 23:59."));
    }

    // =================== Helpers ===================

    private void createUser(String fullName, String username, String password, String role) {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "fullName", fullName,
                        "username", username,
                        "password", password,
                        "role", role
                ))
                .when()
                .post("/admin/new")
                .then()
                .statusCode(201);
    }

    private UUID getUserIdByUsername(String username) {
        String json =
                given().queryParam("username", username)
                        .when().get("/admin/get-id")
                        .then().statusCode(200)
                        .extract().asString();
        return UUID.fromString(new JsonPath(json).getString("id"));
    }

    private UUID createTask(String title, String description, UUID teacherId, UUID studentId) {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "title", title,
                        "description", description,
                        "teacherId", teacherId.toString(),
                        "studentId", studentId.toString()
                ))
                .when()
                .post("/task/new")
                .then()
                .statusCode(201);

        // Find task id by title
        String arr =
                given()
                        .when().get("/task/all")
                        .then().statusCode(200).extract().asString();

        JsonPath jp = new JsonPath(arr);
        var titles = jp.getList("title", String.class);
        int idx = titles.indexOf(title);
        if (idx < 0) throw new AssertionError("Task not found by title: " + title + " in: " + arr);
        String idStr = jp.getString("[" + idx + "].id");
        return UUID.fromString(idStr);
    }

    private static UUID extractSubmissionId(ValidatableResponse then) {
        String body = then.extract().asString();
        try {
            String id = new JsonPath(body).getString("id");
            if (id != null) return UUID.fromString(id);
        } catch (Exception ignored) {}
        // Fallback: read the last submission
        String list = given().when().get("/submit/all").then().statusCode(200).extract().asString();
        JsonPath jp = new JsonPath(list);
        int size = jp.getList("$").size();
        return UUID.fromString(jp.getString("[" + (size - 1) + "].id"));
    }

    private static UUID extractQuestionIdFromAll(ValidatableResponse then, String expectedQuestionText) {
        String listJson = given()
                .when().get("/FAQ/all")
                .then().statusCode(200)
                .extract().asString();

        JsonPath jp = new JsonPath(listJson);
        int size = jp.getList("$").size();
        for (int i = 0; i < size; i++) {
            String q = jp.getString("[" + i + "].question");
            if (expectedQuestionText.equals(q)) {
                String idStr = jp.getString("[" + i + "].id");
                if (idStr == null || idStr.isBlank()) idStr = jp.getString("[" + i + "].questionID");
                if (idStr == null || idStr.isBlank()) {
                    throw new AssertionError("Found question by text but no id field. Payload: " + listJson);
                }
                return UUID.fromString(idStr);
            }
        }
        throw new AssertionError("Question not found by text: \"" + expectedQuestionText + "\". Payload: " + listJson);
    }
}
