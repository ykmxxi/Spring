package com.devmaker.dmaker.service;

import static com.devmaker.exception.DMakerErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmaker.dmaker.dto.CreateDeveloper;
import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.repository.DeveloperRepository;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;
import com.devmaker.exception.DMakerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DmakerService {
	private final DeveloperRepository developerRepository;

	@Transactional
	public void createDeveloper(CreateDeveloper.Request request) {
		validateCreateDeveloperRequest(request);

		// business logic start
		Developer developer = Developer.builder()
			.developerLevel(DeveloperLevel.JUNIOR)
			.developerSkillType(DeveloperSkillType.BACK_END)
			.experiencedYears(2)
			.name("Kim")
			.age(28)
			.build();

		developerRepository.save(developer); // 영속화, DB에 저장
		// business logic end
	}

	private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
		// business logic validation
		DeveloperLevel developerLevel = request.getDeveloperLevel();
		Integer experienceYears = request.getExperienceYears();
		if (developerLevel == DeveloperLevel.SENIOR
			&& experienceYears < 10) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNGNIOR
			&& experienceYears < 4 || experienceYears > 10) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		developerRepository.findByMemberId(request.getMemberId())
			.ifPresent((developer -> {
				throw new DMakerException(DUPLICATED_MEMBER_ID);
			}));
	}
}
