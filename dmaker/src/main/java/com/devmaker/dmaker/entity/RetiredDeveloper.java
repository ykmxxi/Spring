package com.devmaker.dmaker.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class) // auditing 기능 사용
public class RetiredDeveloper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Enumerated(EnumType.STRING)
	private DeveloperLevel developerLevel; // 직급

	@Enumerated(EnumType.STRING)
	private DeveloperSkillType developerSkillType; // 분야

	private Integer experiencedYears; // 경력
	private String memberId; // 회사Id
	private String name; // 이름
	private Integer age; // 나이

	@Enumerated(EnumType.STRING)
	private StatusCode statusCode;

	// Entity 가 생성되어 저장될 때 시간이 자동 저장
	@CreatedDate
	private LocalDateTime createdAt;

	// 조회한 Entity 값을 변경할 때 시간이 자동 저장
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
