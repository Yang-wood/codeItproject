package com.codeit.mini.service.book;
  
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.codeit.mini.dto.book.BookDTO;
import com.codeit.mini.dto.book.UpdateBookDTO;
import com.codeit.mini.entity.book.BookEntity;
  

public interface IBookService {
	
	// epub 저장 
	void saveBook(BookDTO bookDTO, String epubFilePath, String originalFileName, int rentPioint) throws IOException; 
	
	// 도서 상세
	BookDTO getBookById(Long bookId) throws Exception;
	
	// 도서 수정
	void modify(UpdateBookDTO updateBookDTO) throws Exception;
	
	// 도서 삭제
	void remove(Long bookId) throws Exception;
	
	// 도서 전체 목록
	List<BookDTO> getAllBooks() throws Exception;
	
	// 도서뷰어 연결
	BookEntity findByBookId(Long bookId) throws Exception;
	
	default BookDTO entityToDto(BookEntity bookEntity) {
		BookDTO bookDTO = BookDTO.builder().bookId(bookEntity.getBookId())
										   .title(bookEntity.getTitle())
										   .author(bookEntity.getAuthor())
										   .publisher(bookEntity.getPublisher())
										   .pubDate(bookEntity.getPubDate() != null ? bookEntity.getPubDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "날짜 미상")
										   .category(bookEntity.getCategory())
										   .description(bookEntity.getDescription())
										   .coverImage(bookEntity.getCoverImg())
										   .epubPath(bookEntity.getEpubPath())
										   .rentPoint(bookEntity.getRentPoint())
										   .rentCount(bookEntity.getRentCount())
										   .wishCount(bookEntity.getWishCount())
										   .avgRating(bookEntity.getAvgRating())
										   .reviewCount(bookEntity.getReviewCount())
										   .build();
		
		return bookDTO;
	}

}
 