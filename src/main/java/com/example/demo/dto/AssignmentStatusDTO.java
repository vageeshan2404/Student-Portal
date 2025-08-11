package com.example.demo.dto;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Submission;

public class AssignmentStatusDTO {

    private Assignment assignment;
    private Submission submission;

    public AssignmentStatusDTO() {
    }

    public AssignmentStatusDTO(Assignment assignment, Submission submission) {
        this.assignment = assignment;
        this.submission = submission;
    }


	public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public boolean isPassed() {
        if (submission == null || submission.getAnswer() == null || assignment == null || assignment.getCorrectAnswer() == null) {
            return false;
        }
        return submission.getAnswer().equalsIgnoreCase(assignment.getCorrectAnswer());
    }
}
