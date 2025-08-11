package com.example.demo.service;

import com.example.demo.entity.AnswerOption;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findByAssignment(Assignment assignment) {
        return questionRepository.findByAssignment(assignment);
    }
    public Question findById(Long id) {
        return questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found with ID: " + id));
    }
    public Question saveQuestion(Question question) {
        if (question.getOptions() != null) {
            for (AnswerOption option : question.getOptions()) {
                option.setQuestion(question); // ✅ Now this makes sense
            }
        }
        return questionRepository.save(question);
    }

	
}
