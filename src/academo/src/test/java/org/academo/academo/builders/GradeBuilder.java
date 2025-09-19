package org.academo.academo.builders;

import org.academo.academo.dto.GradeDTO;
import org.academo.academo.model.Grade;

import java.util.UUID;

public class GradeBuilder {
    private UUID id = UUID.fromString("00000000-0000-0000-0000-00000000A001");
    private double value = 90.0;
    private String feedback = "Good";
    private UUID submissionId = UUID.fromString("00000000-0000-0000-0000-00000000B001");

    public static GradeBuilder aGrade() {
        return new GradeBuilder();
    }

    public GradeBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public GradeBuilder withValue(double value) {
        this.value = value;
        return this;
    }

    public GradeBuilder withFeedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public GradeBuilder withSubmissionId(UUID submissionId) {
        this.submissionId = submissionId;
        return this;
    }

    public Grade asEntity() {
        return new Grade(id, value, feedback, submissionId);
    }

    public GradeDTO asDto() {
        return new GradeDTO(id, value, feedback, submissionId);
    }
}
