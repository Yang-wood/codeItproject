package com.codeit.mini.dto.book;
  
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
  
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDTO { 
	private Long bookId;
	private String title; 
	private String author; 
	private String publisher; 
	private String pubDate; 
	private String category; 
	private String description; 
	private Integer rentPoint; 
	private Integer rentCount; 
	private Integer wishCount; 
	private Integer reviewCount; 
	private Double avgRating; 
	private String epubPath; // 서버에 EPUB 파일이 저장될 최종 경로 (DB 저장 시 사용) 
	private String coverImageDataBase64; // 웹 페이지 미리보기를 위한 Base64 인코딩된 이미지 문자열 (HTML에서 사용) 
	private String coverImage; // 서버에 저장될 표지 이미지의 최종 경로 (DB 저장 시 사용) 
	private byte[] coverImageData; // 표지 이미지의  실제 바이너리 데이터 (서비스에서 파일 저장 시 사용)
	
	private boolean rentedByCurrentUser;
	private boolean wishedByCurrentUser;
}
 