package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "submissions1")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String answer;

   

    @Column(name = "marks")
    private Integer marks;



    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentPortal student;

    
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<StudentAnswer> answers = new ArrayList<>();

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "wrong_count")
    private Integer wrongCount;

    
    
    public Submission() {
    }

	public Submission(Long id, String answer, Integer marks, Integer score, Assignment assignment,
			StudentPortal student, List<StudentAnswer> answers, Integer correctCount, Integer wrongCount) {
		super();
		this.id = id;
		this.answer = answer;
		this.marks = marks;
		this.score = score;
		this.assignment = assignment;
		this.student = student;
		this.answers = answers;
		this.correctCount = correctCount;
		this.wrongCount = wrongCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public StudentPortal getStudent() {
		return student;
	}

	public void setStudent(StudentPortal student) {
		this.student = student;
	}

	public Integer getMarks() {
		return marks;
	}

	public void setMarks(Integer marks) {
		this.marks = marks;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<StudentAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<StudentAnswer> answers) {
		this.answers = answers;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

}
