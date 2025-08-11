package com.example.demo.service;

import com.example.demo.entity.StudentAnswer;
import com.example.demo.repository.StudentAnswerRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerService {
    private final StudentAnswerRepository studentAnswerRepository;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public StudentAnswer save(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }
}
