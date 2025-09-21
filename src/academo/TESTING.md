## UNIT (offline) :: report

```bash
rm -rf target
mvn clean test -Dgroups=unit -DexcludedGroups=db -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## DB (Testcontainers with in-memory(lab_01)) :: single report

```bash
rm -rf target/allure-results target/site/allure-maven-plugin
mvn test -Dgroups=db -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## Combined (unit + db) :: single report

```bash
rm -rf target
mvn clean test -Dgroups="unit,db" -Dallure.results.directory=target/allure-results
mvn io.qameta.allure:allure-maven:2.12.0:report
```

## JVM process experiments — count forks 

```bash
# Single JVM for UNIT (expect output: 1)
mvn -Psingle-jvm clean test -Dgroups=unit -DexcludedGroups=db -X | grep -c "Forking command line"

# One JVM per TEST CLASS for DB ITs (≈ number of test classes executed)
mvn -Pper-class-fork clean test -Dgroups=db -X | grep -c "Forking command line"

# Parallel forks for DB ITs (≈ number of concurrent forks created up front)
mvn -Pparallel-forks clean test -Dgroups=db -X | grep -c "Forking command line"
```

## JVM process experiments — show each fork line

```bash
mvn -Psingle-jvm clean test -Dgroups=unit -DexcludedGroups=db -X | grep "Forking command line"
mvn -Pper-class-fork clean test -Dgroups=db -X | grep "Forking command line"
mvn -Pparallel-forks clean test -Dgroups=db -X | grep "Forking command line"
```


# Integration Tests (Container with Postgres)
```bash
mvn  clean test-compile -Dit.test=MvpE2EITCase failsafe:integration-test failsafe:verify
```

# Run all tests with Docker
# Build the test image


# LAB 2
## UNIT TESTS
```bash
mvn clean test -Dgroups=unit -DexcludedGroups=db
```
## DB TESTS IN MEMORY
```bash
mvn test -Dgroups=db
```


# DO NOT RUN IT, IT WILL TAKE LONG TIME
```bash
docker build -f Dockerfile.test -t academo-tests:latest .
```

# Run tests inside the container, sharing your host docker for Testcontainers
```bash
docker run --rm \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v "$HOME/.m2:/root/.m2" \
  -v "$PWD/target:/app/target" \
  --add-host=host.docker.internal:host-gateway \
  -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
  academo-tests:latest

```

## CI pipeline (GitHub Actions)
- Stages: **unit → db → e2e** (fail-fast)
- Artifacts:
    - `unit-reports`: surefire + Allure results
    - `db-reports`: surefire + Allure results
    - `e2e-reports`: failsafe + `target/e2e-http.log`

# Running the endpoints in web browser
```bash
mitmweb -r captures/academo.mitm -p 8082 --web-port 9090
```

