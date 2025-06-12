  package com.codeit.mini.entity.epub;
  
  import java.io.File;
  import java.io.IOException;
  
  public interface EpubService { 
	  BookDTO bookDTO(File epubfile) throws IOException; 
	  }
 