package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	private AdminRepository adminRepository;
	
	
	
	public AdminServiceImpl(AdminRepository adminRepository) {
		super();
		this.adminRepository = adminRepository;
	}



	@Override
	public Admin getStudentById(Long id) {
		return adminRepository.findById(id).get();
	}

	@Override
	public Admin saveLogin(Admin admin) {
		return adminRepository.save(admin);
	}



	@Override
	public Admin findByAdminNameAndPassword(String adminName, String password) {
		return adminRepository.findByAdminNameAndPassword(adminName, password);
		   	}

}
