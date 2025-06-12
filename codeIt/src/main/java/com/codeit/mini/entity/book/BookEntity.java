package com.codeit.mini.entity.book;

import java.time.LocalDateTime;

import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(
		name = "BOOK_SEQ_GEN", 
		sequenceName = "book_seq", 
		initialValue = 1,
		allocationSize = 1) 
@Table(name = "BOOK")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookEntity extends BaseEntity {

@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_GEN")
@Column(name = "book_id") // 데이터베이스 컬럼 이름을 지정합니다. 
private Long book_id; // 책고유 ID

@Column(name = "title", nullable = false, length = 500) 
private String title; // 책 제목

@Column(name = "title_nospace", nullable = false, length = 500) 
private String title_nospace; // 검색 효율을 위해 제목에서 공백을 제거한 버전

@Column(name = "author", nullable = false, length = 200) 
private String author; // 작가 이름

@Column(name = "author_nospace", nullable = false, length = 200)
private String author_nospace; // 검색 효율을 위해 작가 이름에서 공백을 제거한 버전

@Column(name = "publisher", length = 200) 
private String publisher; // 출판사

@Column(name = "pub_date") 
private LocalDateTime pub_date; // 출판일 (날짜만)

@Column(name = "category", length = 300) 
private String category; // 카테고리/장르

@Lob 
@Column(name = "description") 
private String description; // 책 상세 설명

@Column(name = "cover_img", length = 500) 
private String cover_img; // 표지 이미지 파일의 저장 경로

@Column(name = "epub_path", nullable = false, length = 500)
private String epub_path; // EPUB 파일 자체의 저장 경로

@Column(name = "rent_point", nullable = false)
private Integer rent_point = 0; // 책 대여 시 필요한 포인트

@Column(name = "rent_count", nullable = false)
private Integer rent_count = 0; // 총 대여 횟수

@Column(name = "wish_count", nullable = false)
private Integer wish_count = 0;  // 찜하기 횟수

@Column(name = "avg_rating", nullable = false)
private Double avg_rating =  0.0; // 평균 별점

@Column(name = "review_count", nullable = false)
private Integer review_count = 0; // 리뷰 개수

}
