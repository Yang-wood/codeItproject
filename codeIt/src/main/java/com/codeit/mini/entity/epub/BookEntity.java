
  package com.codeit.mini.entity.epub;
  
  import java.time.LocalDateTime;
  
  import jakarta.persistence.Column; 
  import jakarta.persistence.Entity; 
  import jakarta.persistence.GeneratedValue; 
  import jakarta.persistence.GenerationType;
  import jakarta.persistence.Id; 
  import jakarta.persistence.Lob;
  import jakarta.persistence.PrePersist; 
  import jakarta.persistence.PreUpdate; 
  import jakarta.persistence.SequenceGenerator;
  import jakarta.persistence.Table;
  import lombok.AllArgsConstructor; 
  import lombok.Data; 
  import lombok.NoArgsConstructor;
  
  @Entity
  @Table(name = "BOOK")
  @AllArgsConstructor
  @NoArgsConstructor
  @Data 
  public class BookEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
  // 기본 키 생성 전략을 SEQUENCE로 설정하고, 사용할 시퀀스 제너레이터를 지정합니다.
  @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ", allocationSize = 1) 
  // Oracle DB 시퀀스 사용을 위한 설정
  @Column(name = "BOOK_ID") // 데이터베이스 컬럼 이름을 지정합니다. 
  private Long bookId; // 책고유 ID
  
  @Column(name = "TITLE", nullable = false, length = 500) 
  // null을 허용하지 않고, 최대 길이를 500으로 설정 
  private String title; // 책 제목
  
  @Column(name = "TITLE_NOSPACE", nullable = false, length = 500) 
  private String titleNospace; // 검색 효율을 위해 제목에서 공백을 제거한 버전
  
  @Column(name = "AUTHOR", nullable = false, length = 200) 
  private String author; // 작가 이름
  
  @Column(name = "AUTHOR_NOSPACE", nullable = false, length = 200)
  private String authorNospace; // 검색 효율을 위해 작가 이름에서 공백을 제거한 버전
  
  @Column(name = "PUBLISHER", length = 200) 
  private String publisher; // 출판사
  
  @Column(name = "PUB_DATE") 
  private LocalDateTime pubDate; // 출판일 (날짜만)
  
  @Column(name = "CATEGORY", length = 300) 
  private String category; // 카테고리/장르
  
  @Lob // Large Object (CLOB/BLOB) 타입을 매핑합니다. 여기서는 긴 텍스트(CLOB)를 위해 사용합니다.
  @Column(name = "DESCRIPTION") 
  private String description; // 책 상세 설명
  
  @Column(name = "COVER_IMG", length = 500) 
  private String coverImg; // 표지 이미지 파일의 저장 경로
  
  @Column(name = "EPUB_PATH", nullable = false, length = 500)
  private String epubPath; // EPUB 파일 자체의 저장 경로
  
  @Column(name = "RENT_POINT", nullable = false)
  private Integer rentPoint = 0; // 책 대여 시 필요한 포인트
  
  @Column(name = "RENT_COUNT", nullable = false)
  private Integer rentCount = 0; // 총 대여 횟수
  
  @Column(name = "WISH_COUNT", nullable = false)
  private Integer wishCount = 0;  // 찜하기 횟수
  
  @Column(name = "AVG_RATING", nullable = false)
  private Double avgRating =  0.0; // 평균 별점
  
  @Column(name = "REVIEW_COUNT", nullable = false)
  private Integer reviewCount = 0; // 리뷰 개수
  
  @Column(name = "REGDATE", nullable = false, updatable = false)
  // 최초 등록일 (생성 이후 업데이트 불가)
  private LocalDateTime regDate = LocalDateTime.now();
  
  @Column(name = "UPDATEDATE") // 마지막 수정일 private 
  LocalDateTime updateDate = LocalDateTime.now();
  
  @PrePersist // 엔터티가 데이터베이스에 저장되기 전에 실행되는 메서드
  public void prePersist() { 
	  if(regDate == null) { 
		  regDate = LocalDateTime.now(); 
		  } 
	  if (updateDate == null) {
		  updateDate = LocalDateTime.now(); 
		  } 
	  }
  
  @PreUpdate // 엔터티가 데이터베이스에서 업데이트되기 전에 실행되는 메서드
  public void preUpdate() {
	  updateDate = LocalDateTime.now(); 
  	} 
  }
 