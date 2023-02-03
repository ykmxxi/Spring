package com.devmaker.dmaker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.dto.DeveloperDetailDto;
import com.devmaker.dmaker.entity.Developer;
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

	@Test
	void testSomething() {
		given(developerRepository.findByMemberId(anyString()))
			.willReturn(Optional.of(Developer.builder()
				.developerLevel(DeveloperLevel.SENIOR)
				.developerSkillType(DeveloperSkillType.FRONT_END)
				.experiencedYears(12)
				.statusCode(StatusCode.EMPLOYED)
				.name("name")
				.age(12)
				.build()));

		DeveloperDetailDto developerDetail = dmakerService.getDeveloperDetail("memberId");

		assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
		assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperSkillType());
		assertEquals(12, developerDetail.getExperiencedYears());
	}

}