package com.codeit.mini.repository.omr;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeit.mini.entity.omr.CategoryEntity;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long>{

}
