
  package com.codeit.mini.dto.book;
  
  import lombok.AllArgsConstructor; 
  import lombok.Data; 
  import lombok.NoArgsConstructor;
  
  @Data
  
  @AllArgsConstructor
  
  @NoArgsConstructor
  public class BookDTO { 
	  private String title; 
	  private String author; 
	  private String publisher; 
	  private String pubDate; 
	  private String category; 
	  private String description; 
	  private String epubPath; // 서버에 EPUB 파일이 저장될 최종 경로 (DB 저장 시 사용) 
	  private String coverImageDataBase64; // 웹 페이지 미리보기를 위한 Base64 인코딩된 이미지 문자열 (HTML에서 사용) 
	  private String coverImageName; // 서버에 저장될 표지 이미지의 최종 경로 (DB 저장 시 사용) 
	  private byte[] coverImageData; // 표지 이미지의  실제 바이너리 데이터 (서비스에서 파일 저장 시 사용)

	  }
 