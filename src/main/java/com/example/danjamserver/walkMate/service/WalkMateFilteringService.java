package com.example.danjamserver.walkMate.service;

import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.walkMate.dto.WalkMateFilteringDTO;
import com.example.danjamserver.walkMate.dto.WalkMateProfileDTO;
import com.example.danjamserver.walkMate.repository.WalkMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WalkMateFilteringService {
    private final WalkMateProfileRepository walkMateProfileRepository;

    // 산책 메이트 후보자 필터링
    // 산책 메이트 후보자 조회 (학교 정보, 성별, 생년월일, 입학년도, MBTI 필터링)
    @Cacheable(
            key = "#schoolId + '_' + #walkMateFilteringDTO.mbti + '_' + " +
                    "(#gender ?: '') + '_' + " +
                    "(#walkMateFilteringDTO.minBirthYear ?: '') + '_' + (#walkMateFilteringDTO.maxBirthYear ?: '') + '_' + " +
                    "(#walkMateFilteringDTO.minEntryYear ?: '') + '_' + (#walkMateFilteringDTO.maxEntryYear ?: '') + '_' + " +
                    "(#walkMateFilteringDTO.colleges ?: '') + '_' + (#walkMateFilteringDTO.preferredWalkTimes ?: '') + '_' + " +
                    "(#walkMateFilteringDTO.preferredWalkTimeZones ?: '') + '_' + (#walkMateFilteringDTO.preferredWalkIntensities ?: '')",
            value = "walkMateCandidateList"
    )
    public List<WalkMateProfileDTO> getFilteredCandidates(WalkMateFilteringDTO walkMateFilteringDTO, Long schoolId, Integer gender){
        // 산책 메이트 후보자 조회 (학교 정보, 성별, 생년월일, 입학년도, MBTI 필터링)
        List<WalkMateProfile> profilesByFilters = walkMateProfileRepository.findProfilesByFilters(
                schoolId,
                gender,
                walkMateFilteringDTO.getMinBirthYear(),
                walkMateFilteringDTO.getMaxBirthYear(),
                walkMateFilteringDTO.getMinEntryYear(),
                walkMateFilteringDTO.getMaxEntryYear(),
                walkMateFilteringDTO.getMbti(),
                walkMateFilteringDTO.getPreferredWalkTimes(),
                walkMateFilteringDTO.getColleges(),
                walkMateFilteringDTO.getPreferredWalkTimeZones(),
                walkMateFilteringDTO.getPreferredWalkIntensities()
        );
        int size = 500; // 1차로 db에서 500명을 가져옴.
        Random random = new Random();
        //500개를 초과하면 랜덤으로 500명을 뽑음. 500개 이하이면 그대로 반환
        if(profilesByFilters.size() > size){
            int randomStartIndex = random.nextInt(profilesByFilters.size() - size);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + size);
        }
        return WalkMateProfileDTO.of(profilesByFilters);
    }
}
