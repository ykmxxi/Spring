package com.devmaker.dmaker.service;

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
class DmakerServiceTest {
	@Mock
	private DeveloperRepository developerRepository;

	@Mock
	private RetiredDeveloperRepository retiredDeveloperRepository;

	@InjectMocks
	// 위에서 선언한 두 개의 Mock 을 빈에 주입
	private DmakerService dmakerService;

	private final Developer defaultDeveloper = Developer.builder()
		.developerLevel(DeveloperLevel.SENIOR)
		.developerSkillType(DeveloperSkillType.FRONT_END)
		.experienceYears(12)
		.statusCode(StatusCode.EMPLOYED)
		.name("name")
		.age(12)
		.build();

	private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
		.developerLevel(DeveloperLevel.SENIOR)
		.developerSkillType(DeveloperSkillType.FRONT_END)
		.experienceYears(12)
		.memberId("memberId")
		.name("name")
		.age(35)
		.build();

	@Test
	void testSomething() {
		// given
		given(developerRepository.findByMemberId(anyString()))
			.willReturn(Optional.of(defaultDeveloper));

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
		given(developerRepository.findByMemberId(anyString()))
			.willReturn(Optional.empty());
		// save 된 내용을 capture
		ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

		// when
		CreateDeveloper.Response developer = dmakerService.createDeveloper(defaultCreateRequest);

		// then
		verify(developerRepository, times(1))
			.save(captor.capture());
		Developer savedDeveloper = captor.getValue();
		assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
		assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
		assertEquals(12, savedDeveloper.getExperienceYears());
	}

	@Test
	void createDeveloperTest_failed_with_duplicated() {
		// given
		given(developerRepository.findByMemberId(anyString()))
			.willReturn(Optional.of(defaultDeveloper));

		// when
		// then
		DMakerException dMakerException = assertThrows(DMakerException.class,
			() -> dmakerService.createDeveloper(defaultCreateRequest)
		);

		assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
	}
}