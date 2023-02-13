package com.devmaker.dmaker.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

public class CreateDeveloper {

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
		@NotNull
		@Min(0) // 최소 0 이상
		@Max(20) // 최대 20 이하
		private Integer experienceYears;

		@NotNull
		@Size(min = 3, max = 50, message = "memberId size must 3 to 50")
		private String memberId;
		@NotNull
		@Size(min = 3, max = 20, message = "name size must 3 to 20")
		private String name;

		@Min(18)
		private Integer age;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Response {
		private DeveloperLevel developerLevel;
		private DeveloperSkillType developerSkillType;
		private Integer experienceYears;
		private String memberId;

		public static Response fromEntity(@NonNull Developer developer) {
			return Response.builder()
				.developerLevel(developer.getDeveloperLevel())
				.developerSkillType(developer.getDeveloperSkillType())
				.experienceYears(developer.getExperienceYears())
				.memberId(developer.getMemberId())
				.build();
		}
	}
}
