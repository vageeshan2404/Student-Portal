package com.example.demo.service;

import com.example.demo.entity.Submission;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.StudentPortal;

import java.util.List;
import java.util.Optional;

public interface SubmissionService {
    List<Submission> getSubmissionsByAssignment(Assignment assignment);

    void saveSubmission(Long assignmentId, Long id, String selectedAnswer);

    List<Submission> findAll();

    Optional<Submission> findLatestByAssignmentAndStudent(Assignment assignment, StudentPortal student);

    Optional<Submission> findTopByAssignmentAndStudentOrderByIdDesc(Assignment assignment, StudentPortal student);

	void save(Submission submission);
	
	
	Submission findByAssignmentAndStudent(Long assignmentId, Long studentId);

}
