
  package com.codeit.mini.entity.epub;
  
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;
  
  @Repository 
  public interface BookRepository extends JpaRepository<BookEntity, Long> {
  
  }
 