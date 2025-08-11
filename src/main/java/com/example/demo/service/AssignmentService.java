package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Course;
import com.example.demo.entity.StudentPortal;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    Assignment createAssignment(Assignment assignment);
    Assignment getAssignmentById(Long id);
    List<Assignment> getAssignmentsByCourse(Course course);
    List<Assignment> getAssignmentsByCourseId(Long courseId);
    List<Assignment> getAssignmentsByAdminId(Long adminId);
    void deleteAssignment(Long id);
    
    
    Assignment saveAssignment(Assignment assignment);
    Optional<Assignment> findById(Long id);
    
    List<Assignment> findAssignmentsByStudent(StudentPortal student);
    
    

}