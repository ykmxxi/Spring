package com.devmaker.dmaker.dto;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperDetailDto {
	private DeveloperLevel developerLevel; // 직급
	private DeveloperSkillType developerSkillType; // 분야
	private Integer experiencedYears; // 경력
	private String memberId; // 회사Id
	private String name; // 이름
	private Integer age; // 나이
	private StatusCode statusCode;

	public static DeveloperDetailDto fromEntity(Developer developer) {
		return DeveloperDetailDto.builder()
			.developerLevel(developer.getDeveloperLevel())
			.developerSkillType(developer.getDeveloperSkillType())
			.experiencedYears(developer.getExperiencedYears())
			.memberId(developer.getMemberId())
			.name(developer.getName())
			.age(developer.getAge())
			.statusCode(developer.getStatusCode())
			.build();
	}
}
