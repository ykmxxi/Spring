package com.devmaker.dmaker.service;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmaker.dmaker.dto.CreateDeveloper;
import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.repository.DeveloperRepository;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DmakerService {
	private final DeveloperRepository developerRepository;
	private final EntityManager em;

	@Transactional
	public void createDeveloper(CreateDeveloper.Request request) {
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
}
