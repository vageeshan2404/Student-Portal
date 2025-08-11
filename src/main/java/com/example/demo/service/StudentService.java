package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.StudentPortal;

public interface StudentService {
    List<StudentPortal> getAllStudents();
    StudentPortal getStudentById(Long id);
    StudentPortal saveStudent(StudentPortal student);
    void deleteStudent(Long id);
    StudentPortal findByEmailAndPassword(String email, String password);
    
    
}