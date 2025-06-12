package com.codeit.mini.entity.admin;

import java.time.LocalDateTime;

import com.codeit.mini.entity.book.BookEntity;
import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(name = "ADMIN_SEQ_GEN",
				   sequenceName = "ADMIN_seq",
				   initialValue = 1,
				   allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Admin extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADMIN_SEQ_GEN")
	@Column(name = "admin_id")
	private Long adminId;
	
	@Column(name = "admin_login_id", nullable = false, unique = true, length = 30)
	private String adminLoginId;
	
	@Column(name = "admin_pw", nullable = false, length = 200)
	private String adminPw;
	
	@Column(name = "admin_name", nullable = false, length = 50)
	private String adminName;
	
	@Column(name = "admin_Email", nullable = false, unique = true, length = 100)
	private String adminEmail;
	
	
	
	
	
	
	
}












