package com.example.demo.service.impl;

import com.example.demo.entity.Submission;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.StudentPortal;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.SubmissionService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentPortalRepository;

    @Override
    public List<Submission> getSubmissionsByAssignment(Assignment assignment) {
        return submissionRepository.findByAssignment(assignment);
    }


    @Override
    public void saveSubmission(Long assignmentId, Long studentId, String answer) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid assignment ID"));
        StudentPortal student = studentPortalRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setAnswer(answer);
        
        submissionRepository.save(submission);
    }


    @Override
    public Submission findByAssignmentAndStudent(Long assignmentId, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new RuntimeException("Assignment not found"));

        StudentPortal student = studentPortalRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

        return submissionRepository.findTopByAssignmentAndStudentOrderByIdDesc(assignment, student)
                .orElse(null);
    }


	@Override
	public List<Submission> findAll() {
		return submissionRepository.findAll();
	}

	@Override
	public Optional<Submission> findLatestByAssignmentAndStudent(Assignment assignment, StudentPortal student) {
	    return submissionRepository.findTopByAssignmentAndStudentOrderByIdDesc(assignment, student);
	}

	@Override
	public Optional<Submission> findTopByAssignmentAndStudentOrderByIdDesc(Assignment assignment, StudentPortal student) {
	    return submissionRepository.findTopByAssignmentAndStudentOrderByIdDesc(assignment, student);
	}
	
	@Override
	public void save(Submission submission) {
	    submissionRepository.save(submission);
	}



	
	
}
