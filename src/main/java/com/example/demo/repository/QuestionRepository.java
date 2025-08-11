package com.example.demo.repository;

import com.example.demo.entity.Question;
import com.example.demo.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssignment(Assignment assignment);
}
