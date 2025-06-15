package com.codeit.mini.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeit.mini.entity.book.ReviewEntity;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewEntity, Long>{

}
