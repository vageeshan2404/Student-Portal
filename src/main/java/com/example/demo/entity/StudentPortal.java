package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="students")
public class StudentPortal {
	
	public StudentPortal() {
		
	}
	
	public StudentPortal(String studentName, String email, String password, String gender, String department,
			List<String> programmingLanguages, String resumePath) {
		super();
		this.studentName = studentName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.department = department;
		this.programmingLanguages = programmingLanguages;
		this.resumePath = resumePath;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="studentName",nullable = false)
	private String studentName;
	
	@Column(name="email", unique=true,nullable = false)
	private String email;
	
	@Column(name="password",nullable = false)
	private String password;
	
	@Column(name="gender",nullable = false)
	private String gender;
	
	@Column(name="department",nullable = false)
	private String department;
	
	@ElementCollection
	@CollectionTable(name = "student_programming_languages", joinColumns = @JoinColumn(name = "student_id"))
	@Column(name = "language")
	private List<String> programmingLanguages;
    
    @Column(nullable = true)
    private String resumePath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<String> getProgrammingLanguages() {
		return programmingLanguages;
	}

	public void setProgrammingLanguages(List<String> programmingLanguages) {
		this.programmingLanguages = programmingLanguages;
	}

	public String getResumePath() {
		return resumePath;
	}

	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
	
	public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }

	
}
