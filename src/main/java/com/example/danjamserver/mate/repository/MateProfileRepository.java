package com.example.danjamserver.mate.repository;

import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.mate.domain.MateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MateProfileRepository extends JpaRepository<MateProfile, Long> {

    // userId와 mateType을 통해 프로필을 찾기
    List<MateProfile> findAllByUserId(Long userId);

    Optional<MateProfile> findByUserIdAndMateType(Long userId, MateType mateType);
}
