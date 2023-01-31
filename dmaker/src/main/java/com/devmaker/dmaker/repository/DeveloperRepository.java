package com.devmaker.dmaker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devmaker.dmaker.code.StatusCode;
import com.devmaker.dmaker.entity.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
	Optional<Developer> findByMemberId(String memberId);

	List<Developer> findDevelopersByStatusCodeEquals(StatusCode statusCode);
}
