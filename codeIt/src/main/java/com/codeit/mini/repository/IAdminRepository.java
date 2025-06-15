package com.codeit.mini.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.admin.Admin;

public interface IAdminRepository extends JpaRepository<Admin, Long> {

}
