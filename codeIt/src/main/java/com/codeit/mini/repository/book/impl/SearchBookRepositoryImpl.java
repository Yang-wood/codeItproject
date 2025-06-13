package com.codeit.mini.repository.book.impl;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.QBookEntity;
import com.codeit.mini.repository.book.IBookSearchRepository;
import com.querydsl.jpa.JPQLQuery;

@Repository
public class SearchBookRepositoryImpl extends QuerydslRepositorySupport implements IBookSearchRepository{

	public SearchBookRepositoryImpl() {
		super(BookEntity.class);
	}

	@Override
	public List<BookEntity> searchBook(String type, String keyword) {
		
		QBookEntity qBook = QBookEntity.bookEntity;
		
		JPQLQuery<BookEntity> query = from(qBook);
		
		if (!StringUtils.hasText(keyword)) {
			
			return query.fetch();
		}
		
		if (StringUtils.hasText(keyword)) {
			 if (type.equalsIgnoreCase("all") || !StringUtils.hasText(type)) {
				 query.where(
							qBook.title.containsIgnoreCase(keyword)
							.or(qBook.author.containsIgnoreCase(keyword))
							.or(qBook.category.containsIgnoreCase(keyword))
						);
			} else if (type.equalsIgnoreCase("title")) {
				query.where(qBook.title.containsIgnoreCase(keyword));
			} else if (type.equalsIgnoreCase("author")) {
				query.where(qBook.author.containsIgnoreCase(keyword));
			} else if (type.equalsIgnoreCase("category")) {
				query.where(qBook.category.containsIgnoreCase(keyword));
			}
		}
		
		return query.fetch();
	}

}
