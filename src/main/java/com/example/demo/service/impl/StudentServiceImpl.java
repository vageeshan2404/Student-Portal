package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.StudentPortal;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentPortal> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @Override
    public StudentPortal getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public StudentPortal saveStudent(StudentPortal student) {
        return studentRepository.save(student);
    }

    @Override
    public StudentPortal findByEmailAndPassword(String email, String password) {
        return studentRepository.findByEmailAndPassword(email, password);
    }

	@Override
	public void deleteStudent(Long id) {
		studentRepository.deleteById(id);
		
		}
	
	
}