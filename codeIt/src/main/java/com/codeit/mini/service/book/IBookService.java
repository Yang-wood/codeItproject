
  package com.codeit.mini.service.book;
  
  import java.io.IOException;
  
  import org.springframework.stereotype.Service;
  import org.springframework.web.multipart.MultipartFile;

import com.codeit.mini.dto.book.BookDTO;
  
  @Service 
  public interface IBookService { 
	  // BookDTO와 임시 EPUB 파일의 경로, 원본 파일명을 받습니다. 
	  void saveBook(BookDTO bookDTO, String epubFilePath, String originalFileName) throws IOException; 
	  }
 