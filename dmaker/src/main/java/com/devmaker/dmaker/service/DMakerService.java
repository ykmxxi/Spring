package com.devmaker.dmaker.service;

import static com.devmaker.dmaker.constant.DMakerConstant.*;
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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DMakerService {
	private final DeveloperRepository developerRepository;
	private final RetiredDeveloperRepository retiredDeveloperRepository;

	@Transactional
	public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
		validateCreateDeveloperRequest(request);

		// business logic start
		return CreateDeveloper.Response.fromEntity(
			developerRepository.save(createDeveloperFromRequest(request))
		);
	}

	private Developer createDeveloperFromRequest(CreateDeveloper.Request request) {
		return Developer.builder()
			.developerLevel(request.getDeveloperLevel())
			.developerSkillType(request.getDeveloperSkillType())
			.experienceYears(request.getExperienceYears())
			.memberId(request.getMemberId())
			.name(request.getName())
			.age(request.getAge())
			.statusCode(StatusCode.EMPLOYED)
			.build();
	}

	private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {
		// business logic validation
		validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

		developerRepository.findByMemberId(request.getMemberId())
			.ifPresent((developer -> {
				throw new DMakerException(DUPLICATED_MEMBER_ID);
			}));
	}

	private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
		if (developerLevel == DeveloperLevel.SENIOR
			&& experienceYears < MIN_SENIOR_EXPERIENCE_YEARS) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNGNIOR
			&& (experienceYears < MAX_JUNIOR_EXPERIENCE_YEARS || experienceYears > MIN_SENIOR_EXPERIENCE_YEARS)) {
			throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
		}

		if (developerLevel == DeveloperLevel.JUNIOR
			&& experienceYears > MAX_JUNIOR_EXPERIENCE_YEARS) {
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
		return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
	}

	private Developer getDeveloperByMemberId(String memberId) {
		return developerRepository.findByMemberId(memberId)
			.orElseThrow(() -> new DMakerException(NO_DEVELOPER));
	}

	@Transactional
	public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
		validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

		return DeveloperDetailDto.fromEntity(
			getUpdatedDeveloperFromRequest(
				request, getDeveloperByMemberId(memberId)
			)
		);
	}

	private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
		developer.setDeveloperLevel(request.getDeveloperLevel());
		developer.setDeveloperSkillType(request.getDeveloperSkillType());
		developer.setExperienceYears(request.getExperienceYears());

		return developer;
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
