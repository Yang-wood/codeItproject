  package com.codeit.mini.service.book;
  
  import java.io.File;
import java.io.IOException;

import com.codeit.mini.dto.book.BookDTO;
  
  public interface EpubService { 
	  BookDTO bookDTO(File epubfile) throws IOException; 
	  }
 