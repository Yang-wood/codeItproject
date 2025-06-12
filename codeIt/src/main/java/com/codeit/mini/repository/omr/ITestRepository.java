package com.codeit.mini.repository.omr;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.omr.TestEntity;

public interface ITestRepository extends JpaRepository<TestEntity, Long>{

}
