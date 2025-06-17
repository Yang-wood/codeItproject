package com.codeit.mini.repository.book.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.book.QBookEntity;
import com.codeit.mini.repository.book.IBookSearchRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

@Repository
public class SearchBookRepositoryImpl extends QuerydslRepositorySupport implements IBookSearchRepository{

	public SearchBookRepositoryImpl() {
		super(BookEntity.class);
	}

	@Override
	public Page<BookEntity> searchBook(String type, String keyword, Integer point, Pageable pageable) {
		
		QBookEntity qBook = QBookEntity.bookEntity;
		JPQLQuery<BookEntity> query = from(qBook);
		
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if (StringUtils.hasText(keyword)) {
			BooleanExpression conditions = null;
			Integer pointKeyword = null;
			boolean isKeyNum = false;
			
			try {
				pointKeyword = Integer.parseInt(keyword);
				isKeyNum = true;
			} catch (NumberFormatException ignored) {
				
			}
			
			if (type.equalsIgnoreCase("point") && isKeyNum) {
				conditions = qBook.rentPoint.eq(pointKeyword);
			} else if (type.contentEquals("all") || !StringUtils.hasText(type)) {
				conditions = qBook.title.containsIgnoreCase(keyword)
						.or(qBook.author.containsIgnoreCase(keyword))
						.or(qBook.category.containsIgnoreCase(keyword));
				
				if (isKeyNum) {
					conditions = conditions.or(qBook.rentPoint.eq(pointKeyword));
				}
			} else if (type.equalsIgnoreCase("title")) {
				conditions = qBook.title.containsIgnoreCase(keyword);
			} else if (type.equalsIgnoreCase("author")) {
				conditions = qBook.author.containsIgnoreCase(keyword);
			} else if (type.equalsIgnoreCase("category")) {
				conditions = qBook.category.containsIgnoreCase(keyword);
			}
			if (conditions != null) {
				builder.and(conditions);
			}
		}
		
		if (point != null) {
			builder.and(qBook.rentPoint.eq(point));
		}
		
		query.where(builder);
		
		JPQLQuery<BookEntity> pageQuery = getQuerydsl().applyPagination(pageable, query);
		long total = pageQuery.fetchCount();
		List<BookEntity> rs = pageQuery.fetch();
		
		return new PageImpl<>(rs, pageable, total);
	}

}
