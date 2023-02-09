package com.devmaker.dmaker.service;

import static com.devmaker.dmaker.exception.DMakerErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.dto.CreateDeveloper;
import com.devmaker.dmaker.dto.DeveloperDetailDto;
import com.devmaker.dmaker.dto.DeveloperDto;
import com.devmaker.dmaker.dto.EditDeveloper;
import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.entity.RetiredDeveloper;
import com.devmaker.dmaker.exception.DMakerException;
import com.devmaker.dmaker.repository.DeveloperRepository;
import com.devmaker.dmaker.repository.RetiredDeveloperRepository;
import com.devmaker.dmaker.type.DeveloperLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DmakerService {
	private final DeveloperRepository developerRepository;
	private final RetiredDeveloperRepository retiredDeveloperRepository;

	@Transactional
	public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
		validateCreateDeveloperRequest(request);

		// business logic start
		Developer developer = Developer.builder()
			.developerLevel(request.getDeveloperLevel())
			.developerSkillType(request.getDeveloperSkillType())
			.experienceYears(request.getExperienceYears())
			.memberId(request.getMemberId())
			.name(request.getName())
			.age(request.getAge())
			.statusCode(StatusCode.EMPLOYED)
			.build();

		developerRepository.save(developer); // 영속화, DB에 저장

		return CreateDeveloper.Response.fromEntity(developer);
	}

	private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
		// business logic validation
		validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

		developerRepository.findByMemberId(request.getMemberId())
			.ifPresent((developer -> {
				throw new DMakerException(DUPLICATED_MEMBER_ID);
			}));
	}

	private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
		if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNGNIOR && (experienceYears < 4 || experienceYears > 10)) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}
	}

	@Transactional(readOnly = true)
	public List<DeveloperDto> getAllEmployedDevelopers() {
		return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
			.stream()
			.map(DeveloperDto::fromEntity)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public DeveloperDetailDto getDeveloperDetail(String memberId) {
		return developerRepository.findByMemberId(memberId)
			.map(DeveloperDetailDto::fromEntity)
			.orElseThrow(() -> new DMakerException(NO_DEVELOPER));
	}

	@Transactional
	public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
		validateEditDeveloperRequest(request, memberId);

		Developer developer = developerRepository.findByMemberId(memberId)
			.orElseThrow(() -> new DMakerException(NO_DEVELOPER));

		developer.setDeveloperLevel(request.getDeveloperLevel());
		developer.setDeveloperSkillType(request.getDeveloperSkillType());
		developer.setExperienceYears(request.getExperienceYears());

		return DeveloperDetailDto.fromEntity(developer);
	}

	private void validateEditDeveloperRequest(EditDeveloper.Request request, String memberId) {
		validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
	}

	@Transactional
	public DeveloperDetailDto deleteDeveloper(String memberId) {
		// 1. EMPLOYED -> RETIRED
		Developer developer = developerRepository.findByMemberId(memberId)
			.orElseThrow(() -> new DMakerException(NO_DEVELOPER));
		developer.setStatusCode(StatusCode.RETIRED);

		// 2. save into RetiredDeveloper
		RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
			.memberId(memberId)
			.name(developer.getName())
			.build();
		retiredDeveloperRepository.save(retiredDeveloper);
		return DeveloperDetailDto.fromEntity(developer);
	}
}
