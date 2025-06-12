//package com.codeit.mini.repository.book.impl;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//
//import com.codeit.mini.entity.book.BookEntity;
//import com.codeit.mini.entity.book.QBookEntity;
//import com.codeit.mini.repository.book.IBookSearchRepository;
//import com.querydsl.jpa.JPQLQuery;
//
//import lombok.extern.log4j.Log4j2;
//
//@Log4j2
//public class BookSearchRepositoryImpl extends QuerydslRepositorySupport implements IBookSearchRepository {
//	QBookEntity bookEntity = QBookEntity.bookEntity;
//	JPQLQuery<BookEntity> query = from(bookEntity);
//	
//	
//	public BookSearchRepositoryImpl() {
//		super(BookEntity.class);
//	}
//
//	@Override
//	public List<BookEntity> searchKeyword(String keyword) {
//		
//		query.where(bookEntity.titleNospace.containsIgnoreCase(keyword.replaceAll("\\s", ""))
//				.or(bookEntity.authorNospace.containsIgnoreCase(keyword.replaceAll("\\s", ""))));
// 		
//		return query.fetch();
//	}
//
//	@Override
//	public List<BookEntity> searchTitle(String title) {
//		
//		query.where(bookEntity.titleNospace.containsIgnoreCase(title.replaceAll("\\s", "")));
//		
//		return query.fetch();
//	}
//
//	@Override
//	public List<BookEntity> searchAuthor(String author) {
//		
//		query.where(bookEntity.authorNospace.containsIgnoreCase(author.replaceAll("\\s", "")));
//		
//		return query.fetch();
//	}
//	
//}
