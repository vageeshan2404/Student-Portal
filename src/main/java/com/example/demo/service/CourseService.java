package com.example.demo.service;

import com.example.demo.entity.Course;
import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    void deleteCourse(Long id);
	List<Course> getAllCourses1();
	
	int getCourseCount();
}