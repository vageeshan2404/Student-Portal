package com.example.demo.service;

import com.example.demo.entity.Admin;


public interface AdminService {
	Admin getStudentById(Long id);
	
	
	
	Admin saveLogin(Admin admin);
	
	Admin findByAdminNameAndPassword(String adminName, String password);

}
