package com.example.demo.service.impl;

import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.StudentPortal;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.EnrollmentService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
	
    private final EnrollmentRepository enrollmentRepository;
    private StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository=studentRepository;
        this.courseRepository=courseRepository;
    }

   
    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByStudent(StudentPortal student) {
        return enrollmentRepository.findByStudent(student);
    }


    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

   
    
    @Override
    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    
    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
        
    }
    
    @Override
    @Transactional
    public void deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        
        // Remove from both sides of the relationship
        enrollment.getStudent().getEnrollments().removeIf(e -> e.getId().equals(enrollmentId));
        enrollment.getCourse().getEnrollments().removeIf(e -> e.getId().equals(enrollmentId));
        
        enrollmentRepository.deleteById(enrollmentId);
    }
    
    
    
    @Override
    public Enrollment getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    
   
    @Transactional
    @Override
    public Enrollment enrollStudent(Enrollment enrollment) {
        StudentPortal student = studentRepository.findById(enrollment.getStudent().getId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(enrollment.getCourse().getId())
            .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        student.getEnrollments().add(enrollment);
        course.getEnrollments().add(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }


    
}