package com.example.danjamserver.workoutMate.service;

import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import com.example.danjamserver.workoutMate.dto.WorkoutMateFilteringDTO;
import com.example.danjamserver.workoutMate.dto.WorkoutMateProfileDTO;
import com.example.danjamserver.workoutMate.repository.WorkoutMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WorkoutMateFilteringService {

    private final WorkoutMateProfileRepository workoutMateProfileRepository;

    @Cacheable(
            key = "#schoolId + '_' + #workoutMateFilteringDTO.mbti + '_' + " +
                    "(#gender ?: '') + '_' + " +
                    "(#workoutMateFilteringDTO.minBirthYear ?: '') + '_' + (#workoutMateFilteringDTO.maxBirthYear ?: '') + '_' + " +
                    "(#workoutMateFilteringDTO.minEntryYear ?: '') + '_' + (#workoutMateFilteringDTO.maxEntryYear ?: '') + '_' + " +
                    "(#workoutMateFilteringDTO.colleges ?: '') + '_' + (#workoutMateFilteringDTO.preferredWorkoutTimeZones ?: '') + '_' + " +
                    "(#workoutMateFilteringDTO.preferredWorkoutIntensities ?: '') + '_' + (#workoutMateFilteringDTO.preferredWorkoutTimes ?: '') + '_' + " +
                    "(#workoutMateFilteringDTO.preferredWorkoutTypes ?: '')",
            value = "workoutMateCandidateList"
    )
    // 운동 메이트 후보자 필터링
    public List<WorkoutMateProfileDTO> getFilteredCandidates(WorkoutMateFilteringDTO workoutMateFilteringDTO, Long schoolId, Integer gender){
        List<WorkoutMateProfile> profilesByFilters = workoutMateProfileRepository.findProfilesByFilters(
                schoolId,
                gender,
                workoutMateFilteringDTO.getMinBirthYear(),
                workoutMateFilteringDTO.getMaxBirthYear(),
                workoutMateFilteringDTO.getMinEntryYear(),
                workoutMateFilteringDTO.getMaxEntryYear(),
                workoutMateFilteringDTO.getMbti(),
                workoutMateFilteringDTO.getColleges(),
                workoutMateFilteringDTO.getPreferredWorkoutTimeZones(),
                workoutMateFilteringDTO.getPreferredWorkoutIntensities(),
                workoutMateFilteringDTO.getPreferredWorkoutTimes(),
                workoutMateFilteringDTO.getPreferredWorkoutTypes()
        );

        int size = 500;
        Random random = new Random();
        //500개를 초과하면 랜덤으로 500명을 뽑음. 500개 이하이면 그대로 반환
        if(profilesByFilters.size() > size){
            int randomStartIndex = random.nextInt(profilesByFilters.size() - size);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + size);
        }
        return WorkoutMateProfileDTO.of(profilesByFilters);
    }
}
