package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.StudentPortal;

public interface StudentRepository extends JpaRepository<StudentPortal, Long> {
    @Query("SELECT s FROM StudentPortal s WHERE s.email = :email AND s.password = :password")
    StudentPortal findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}