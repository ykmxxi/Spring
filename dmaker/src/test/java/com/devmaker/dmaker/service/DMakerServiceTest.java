package com.devmaker.dmaker.service;

import static com.devmaker.dmaker.constant.DMakerConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.dto.CreateDeveloper;
import com.devmaker.dmaker.dto.DeveloperDetailDto;
import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.exception.DMakerErrorCode;
import com.devmaker.dmaker.exception.DMakerException;
import com.devmaker.dmaker.repository.DeveloperRepository;
import com.devmaker.dmaker.repository.RetiredDeveloperRepository;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
	@Mock
	private DeveloperRepository developerRepository;

	@Mock
	private RetiredDeveloperRepository retiredDeveloperRepository;

	@InjectMocks
	// 위에서 선언한 두 개의 Mock 을 빈에 주입
	private DMakerService dmakerService;

	private final Developer defaultDeveloper = Developer.builder()
		.developerLevel(DeveloperLevel.SENIOR)
		.developerSkillType(DeveloperSkillType.FRONT_END)
		.experienceYears(12)
		.statusCode(StatusCode.EMPLOYED)
		.name("name")
		.age(12)
		.build();

	private CreateDeveloper.Request getCreateRequest(DeveloperLevel developerLevel,
		DeveloperSkillType developerSkillType, Integer experienceYears) {
		return CreateDeveloper.Request.builder()
			.developerLevel(developerLevel)
			.developerSkillType(developerSkillType)
			.experienceYears(experienceYears)
			.memberId("memberId")
			.name("name")
			.age(35)
			.build();
	}

	@Test
	void testSomething() {
		// given
		given(developerRepository.findByMemberId(anyString())).willReturn(Optional.of(defaultDeveloper));

		// when
		DeveloperDetailDto developerDetail = dmakerService.getDeveloperDetail("memberId");

		// then
		assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
		assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperSkillType());
		assertEquals(12, developerDetail.getExperiencedYears());
	}

	@Test
	void createDeveloperTest_success() {
		// given
		given(developerRepository.findByMemberId(anyString())).willReturn(Optional.empty());
		given(developerRepository.save(any())).willReturn(defaultDeveloper);
		// save 된 내용을 capture
		ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

		// when
		CreateDeveloper.Response developer = dmakerService.createDeveloper(
			getCreateRequest(
				DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 2
			)
		);

		// then
		verify(developerRepository, times(1)).save(captor.capture());
		Developer savedDeveloper = captor.getValue();
		assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
		assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
		assertEquals(MIN_SENIOR_EXPERIENCE_YEARS + 2, savedDeveloper.getExperienceYears());
	}

	@Test
	void createDeveloperTest_fail_unmatched_level() {
		// given

		// when
		DMakerException dMakerException = assertThrows(DMakerException.class,
			() -> dmakerService.createDeveloper(
				getCreateRequest(
					DeveloperLevel.JUNIOR, DeveloperSkillType.FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1)
			)
		);

		// then
		assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

		dMakerException = assertThrows(DMakerException.class,
			() -> dmakerService.createDeveloper(
				getCreateRequest(
					DeveloperLevel.JUNGNIOR, DeveloperSkillType.FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1)
			)
		);

		assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

		dMakerException = assertThrows(DMakerException.class,
			() -> dmakerService.createDeveloper(
				getCreateRequest(
					DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1)
			)
		);

		assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
	}

	@Test
	void createDeveloperTest_failed_with_duplicated() {
		// given
		given(developerRepository.findByMemberId(anyString())).willReturn(Optional.of(defaultDeveloper));

		// when
		// then
		DMakerException dMakerException = assertThrows(DMakerException.class, () -> dmakerService.createDeveloper(
			getCreateRequest(DeveloperLevel.SENIOR, DeveloperSkillType.FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1)));

		assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
	}
}