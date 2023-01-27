package com.devmaker.dmaker.service;

import com.devmaker.dmaker.entity.Developer;
import com.devmaker.dmaker.repository.DeveloperRepository;
import com.devmaker.dmaker.type.DeveloperLevel;
import com.devmaker.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DmakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public void createDeveloper() {
        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .experiencedYears(2)
                .name("Kim")
                .age(28)
                .build();

        developerRepository.save(developer); // 영속화, DB에 저장
    }
}
