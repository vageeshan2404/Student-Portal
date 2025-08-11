package com.example.demo.service;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.StudentPortal;

import java.util.List;

public interface EnrollmentService {
    Enrollment enrollStudent(Enrollment enrollment);
    List<Enrollment> getEnrollmentsByCourse(Long courseId);
    List<Enrollment> getEnrollmentsByStudent(Long long1);
    Enrollment getEnrollmentById(Long id);
    void deleteEnrollment(Long id);
    List<Enrollment> getAllEnrollments(); 
    

    List<Enrollment> getEnrollmentsByCourseId(Long courseId);
    
    
    Enrollment getEnrollmentByStudentAndCourse(Long studentId, Long courseId);
    
      
    
}