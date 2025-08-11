package com.example.demo.repository;

import com.example.demo.entity.Enrollment;
import com.example.demo.entity.StudentPortal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);
    
   
    List<Enrollment> findAll();

 
	
	  @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
	    Enrollment findByStudentIdAndCourseId(@Param("studentId") Long studentId, 
	                                        @Param("courseId") Long courseId);



	  
	  
	List<Enrollment> findByStudent(StudentPortal student);

	  
}