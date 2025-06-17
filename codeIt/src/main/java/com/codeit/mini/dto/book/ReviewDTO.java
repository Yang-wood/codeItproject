package com.codeit.mini.dto.book;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDTO {
	
	@NotNull(message = "리뷰 ID는 필수입니다.")
    private Long reviewId;
	
	@NotNull(message = "대여 ID는 필수입니다.")
    private Long rentId;
	
	@NotNull(message = "멤버 ID는 필수입니다.")
    private Long memberId;
	
	@NotNull(message = "도서 ID는 필수입니다.")
    private Long bookId;
	
    @Size(max = 300, message = "제목은 300자를 초과할 수 없습니다.")
    private String title;

    @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
    private String content;

    @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
    private Integer rating;
    
    private LocalDateTime regdate;
    
    private LocalDateTime updateDate;
    
    
    
    
}
