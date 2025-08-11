package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<AnswerOption> options = new ArrayList<>();

   public Question() {
	   
   }

public Question(Long id, String questionText, String correctAnswer, Assignment assignment, List<AnswerOption> options) {
	super();
	this.id = id;
	this.questionText = questionText;
	this.correctAnswer = correctAnswer;
	this.assignment = assignment;
	this.options = options;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getQuestionText() {
	return questionText;
}

public void setQuestionText(String questionText) {
	this.questionText = questionText;
}

public String getCorrectAnswer() {
	return correctAnswer;
}

public void setCorrectAnswer(String correctAnswer) {
	this.correctAnswer = correctAnswer;
}

public Assignment getAssignment() {
	return assignment;
}

public void setAssignment(Assignment assignment) {
	this.assignment = assignment;
}

public List<AnswerOption> getOptions() {
	return options;
}

public void setOptions(List<AnswerOption> options) {
	this.options = options;
}
   
   
}
