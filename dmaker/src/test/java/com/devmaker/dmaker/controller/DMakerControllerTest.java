package com.devmaker.dmaker.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devmaker.dmaker.dto.DeveloperDto;
import com.devmaker.dmaker.service.DMakerService;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

@WebMvcTest(DMakerController.class)
class DMakerControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DMakerService dmakerService;

	protected MediaType contentType = new MediaType(
		MediaType.APPLICATION_JSON.getType(),
		MediaType.APPLICATION_JSON.getSubtype(),
		StandardCharsets.UTF_8);

	@Test
	void getAllDevelopers() throws Exception {
		// given
		DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
			.developerSkillType(DeveloperSkillType.BACK_END)
			.developerLevel(DeveloperLevel.JUNIOR)
			.memberId("memberId1")
			.build();
		DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
			.developerSkillType(DeveloperSkillType.BACK_END)
			.developerLevel(DeveloperLevel.SENIOR)
			.memberId("memberId2")
			.build();

		given(dmakerService.getAllEmployedDevelopers())
			.willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

		// when, then
		mockMvc.perform(get("/developers").contentType(contentType))
			.andExpect(status().isOk())
			.andDo(print()) // 상세한 요청/응답 내용이 출력됨
			.andExpect(
				jsonPath("$.[0].developerSkillType",
					is(DeveloperSkillType.BACK_END.name())))
			.andExpect(
				jsonPath("$.[0].developerLevel",
					is(DeveloperLevel.JUNIOR.name())))
			.andExpect(
				jsonPath("$.[1].developerSkillType",
					is(DeveloperSkillType.BACK_END.name())))
			.andExpect(
				jsonPath("$.[1].developerLevel",
					is(DeveloperLevel.SENIOR.name())));
	}
}