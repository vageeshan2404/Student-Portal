package com.example.demo.service.impl;

import com.example.demo.entity.AnswerOption;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Course;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.StudentPortal;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.service.AssignmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    
	@Autowired
	private final AssignmentRepository assignmentRepository;
	
	private EnrollmentRepository enrollmentRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, EnrollmentRepository enrollmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.enrollmentRepository=enrollmentRepository;

    }

   
    @Override
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

 

    @Override
    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    @Override
    public List<Assignment> getAssignmentsByAdminId(Long adminId) {
        return assignmentRepository.findByAdminId(adminId);
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) { // This method now returns Assignment
        if (assignment.getAnswerOptions() != null) {
            for (AnswerOption option : assignment.getAnswerOptions()) {
            
            }
        }
        return assignmentRepository.save(assignment);
    
    }

    
    @Override
    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }

    
    @Override
    public List<Assignment> findAssignmentsByStudent(StudentPortal student) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        List<Course> courses = enrollments.stream().map(Enrollment::getCourse).toList();
        return assignmentRepository.findByCourseIn(courses);
    }

    @Override
    public List<Assignment> getAssignmentsByCourse(Course course) {
    	return assignmentRepository.findByCourseIn(List.of(course));

    }


    
    
    
}