package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
	@Query("SELECT c FROM Course c LEFT JOIN FETCH c.assignments WHERE c.id = :id")
	Course findCourseWithAssignments(@Param("id") Long id);
}