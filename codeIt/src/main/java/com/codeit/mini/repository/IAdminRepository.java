package com.codeit.mini.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.admin.Admin;

public interface IAdminRepository extends JpaRepository<Admin, Long> {
	
	// 로그인 ID로 관리자 조회 (로그인용)
    Optional<Admin> findByAdminLoginId(String adminLoginId);

}
