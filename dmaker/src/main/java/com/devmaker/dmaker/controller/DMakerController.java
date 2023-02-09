package com.devmaker.dmaker.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devmaker.dmaker.dto.CreateDeveloper;
import com.devmaker.dmaker.dto.DeveloperDetailDto;
import com.devmaker.dmaker.dto.DeveloperDto;
import com.devmaker.dmaker.dto.EditDeveloper;
import com.devmaker.dmaker.service.DmakerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor // 자동으로 생성자를 만들어 Spring Application Context 위에 주입
@Slf4j
@RestController
// @RestController: @Controller 와 @ResponseBody 를 함께 달아주는 annotation
// 사용자 요청을 받아 Json 형태로 응답을 내려준다.
public class DMakerController {
	// service 빈 주입, Spring Application Context 위에 주입
	private final DmakerService dmakerService;

	@GetMapping("/developers")
	public List<DeveloperDto> getAllDevelopers() {
		// GET / developers HTTP/1.1
		log.info("GET / developers HTTP/1.1");

		return dmakerService.getAllEmployedDevelopers();
	}

	@GetMapping("/developer/{memberId}")
	public DeveloperDetailDto getDeveloperDetail(
		@PathVariable final String memberId
	) {
		// GET / developers HTTP/1.1
		log.info("GET / developers HTTP/1.1");

		return dmakerService.getDeveloperDetail(memberId);
	}

	@PostMapping("/create-developer")
	public CreateDeveloper.Response createDevelopers(
		@Valid @RequestBody CreateDeveloper.Request request
	) {
		log.info("request: {}", request);
		return dmakerService.createDeveloper(request);
	}

	// put: 모든 정보 수정, fetch: 리소스 중 특정 데이터 수정
	@PutMapping("/developer/{memberId}")
	public DeveloperDetailDto editDeveloper(
		@PathVariable final String memberId,
		@Valid @RequestBody EditDeveloper.Request request
	) {
		log.info("GET /developers HTTP/1.1");

		return dmakerService.editDeveloper(memberId, request);
	}

	@DeleteMapping("/developer/{memberId}")
	public DeveloperDetailDto deleteDeveloper(
		@PathVariable final String memberId) {
		return dmakerService.deleteDeveloper(memberId);
	}
}
