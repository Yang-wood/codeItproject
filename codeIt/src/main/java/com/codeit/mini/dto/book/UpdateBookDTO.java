package com.codeit.mini.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateBookDTO {
	
	@NotNull(message = "도서 ID는 필수입니다.")
    private Long bookId;

    @Size(max = 300, message = "카테고리는 300자를 초과할 수 없습니다.")
    private String category;

    @Size(max = 2000, message = "설명은 2000자를 초과할 수 없습니다.")
    private String description;

    @Min(value = 0, message = "대여 포인트는 0 이상이어야 합니다.")
    private Integer rentPoint;
}
