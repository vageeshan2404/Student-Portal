package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	@Query("SELECT a FROM Admin a WHERE a.adminName = :adminName AND a.password = :password")
    Admin findByAdminNameAndPassword(@Param("adminName") String adminName, @Param("password") String password);
}
