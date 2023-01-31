package com.devmaker.dmaker.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class EditDeveloper {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class Request {
		@NotNull
		private DeveloperLevel developerLevel;
		@NotNull
		private DeveloperSkillType developerSkillType;
		@NotNull @Min(0) // 최소 0 이상
		@Max(20) // 최대 20 이하
		private Integer experienceYears;
	}
}
