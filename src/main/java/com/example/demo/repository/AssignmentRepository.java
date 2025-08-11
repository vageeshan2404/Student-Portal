package com.example.demo.repository;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Course;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse(Course course);
    
    
    @Query("SELECT a FROM Assignment a WHERE a.course.createdBy.id = :adminId")
    List<Assignment> findByAdminId(@Param("adminId") Long adminId);
    
    @EntityGraph(attributePaths = {"answerOptions"})
    List<Assignment> findByCourseId(Long courseId);
    
    
   
    List<Assignment> findByCourseIn(List<Course> courses);

}