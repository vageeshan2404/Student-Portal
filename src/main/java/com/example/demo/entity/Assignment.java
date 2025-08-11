package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "assignment_title", nullable = false)
    private String assignmentTitle;
    
    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;
    
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    
    
 	@OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> answerOptions = new ArrayList<>();
    
    
    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

	
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    
    public Assignment() {
    	
    }
    
    public Assignment(Long id, String assignmentTitle, String question, List<AnswerOption> answerOptions,
			String correctAnswer, Course course, LocalDateTime createdAt, Submission submission) {
		super();
		this.id = id;
		this.assignmentTitle = assignmentTitle;
		this.question = question;
		this.answerOptions = answerOptions;
		this.correctAnswer = correctAnswer;
		this.course = course;
		this.createdAt = createdAt;
		this.submission = submission;
	}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAssignmentTitle() { return assignmentTitle; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
    
    public String getQuestion() { return question; }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public List<AnswerOption> getAnswerOptions() { return answerOptions; }
    public void setAnswerOptions(List<AnswerOption> answerOptions2) { this.answerOptions = answerOptions2; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


    @Transient 
    private Submission submission;

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }






}