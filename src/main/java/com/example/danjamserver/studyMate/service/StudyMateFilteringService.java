package com.example.danjamserver.studyMate.service;

import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.dto.StudyMateFilteringDTO;
import com.example.danjamserver.studyMate.dto.StudyMateProfileDTO;
import com.example.danjamserver.studyMate.repository.StudyMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StudyMateFilteringService {

    @Autowired
    StudyMateProfileRepository studyMateProfileRepository;

    @Cacheable(
            key = "#schoolId + '_' + #studyMateFilteringDTO.mbti + '_' + " +
                    "(#gender ?: '') + '_' + " +
                    "(#studyMateFilteringDTO.minBirthYear ?: '') + '_' + (#studyMateFilteringDTO.maxBirthYear ?: '') + '_' + " +
                    "(#studyMateFilteringDTO.minEntryYear ?: '') + '_' + (#studyMateFilteringDTO.maxEntryYear ?: '') + '_' + " +
                    "(#studyMateFilteringDTO.colleges ?: '') + '_' + (#studyMateFilteringDTO.preferredStudyTypes ?: '') + '_' + " +
                    "(#studyMateFilteringDTO.preferredStudyTimes ?: '') + '_' + (#studyMateFilteringDTO.preferredAverageGrades ?: '')",
            value = "studyMateCandidateList"
    )
    // 스터디 메이트 후보자 필터링
    // redis cache를 사용하여 필터링된 후보자 리스트를 캐싱
    public List<StudyMateProfileDTO> getFilteredCandidates(StudyMateFilteringDTO studyMateFilteringDTO, Long schoolId, Integer gender) {
        List<StudyMateProfile> profilesByFilters = studyMateProfileRepository.findProfilesByFilters(
                schoolId,
                gender,
                studyMateFilteringDTO.getMinBirthYear(),
                studyMateFilteringDTO.getMaxBirthYear(),
                studyMateFilteringDTO.getMinEntryYear(),
                studyMateFilteringDTO.getMaxEntryYear(),
                studyMateFilteringDTO.getMbti(),
                studyMateFilteringDTO.getColleges(),
                studyMateFilteringDTO.getPreferredStudyTypes(),
                studyMateFilteringDTO.getPreferredStudyTimes(),
                studyMateFilteringDTO.getPreferredAverageGrades()
        );
        int size = 500; // 1차로 db에서 500명을 가져옴.
        Random random = new Random();
        //500개를 초과하면 랜덤으로 500명을 뽑음. 500개 이하이면 그대로 반환
        if(profilesByFilters.size() > size){
            int randomStartIndex = random.nextInt(profilesByFilters.size() - size);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + size);
        }
        return StudyMateProfileDTO.of(profilesByFilters);
    }
}
