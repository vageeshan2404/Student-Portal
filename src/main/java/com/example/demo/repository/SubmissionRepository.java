package com.example.demo.repository;

import com.example.demo.entity.Submission;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.StudentPortal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignment(Assignment assignment);
    
    Optional<Submission> findTopByAssignmentAndStudentOrderByIdDesc(Assignment assignment, StudentPortal student);

    
}
