package com.example.danjamserver.foodMate.service;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.dto.FoodMateFilteringDTO;
import com.example.danjamserver.foodMate.dto.FoodMateProfileDTO;
import com.example.danjamserver.foodMate.repository.FoodMateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FoodMateFilteringService {

    private final FoodMateProfileRepository foodMateProfileRepository;

    @Cacheable(
            key = "#schoolId + '_' + #foodMateFilteringDTO.mbti + '_' + " +
                    "(#gender ?: '') + '_' + " +
                    "(#foodMateFilteringDTO.minBirthYear ?: '') + '_' + (#foodMateFilteringDTO.maxBirthYear ?: '') + '_' + " +
                    "(#foodMateFilteringDTO.minEntryYear ?: '') + '_' + (#foodMateFilteringDTO.maxEntryYear ?: '') + '_' + " +
                    "(#foodMateFilteringDTO.colleges ?: '') + '_' + (#foodMateFilteringDTO.mateTime ?: '') + '_' + " +
                    "(#foodMateFilteringDTO.menus ?: '') + '_' + (#foodMateFilteringDTO.mealTime ?: '') + '_' + " +
                    "(#foodMateFilteringDTO.diningManner ?: '')",
            value = "foodMateCandidateList"
    )
    // 음식 메이트 후보자 필터링
    public List<FoodMateProfileDTO> getFilteredCandidates(FoodMateFilteringDTO foodMateFilteringDTO, Long schoolId, Integer gender){
        List<FoodMateProfile> profilesByFilters = foodMateProfileRepository.findProfilesByFilters(
                schoolId,
                gender,
                foodMateFilteringDTO.getMinBirthYear(),
                foodMateFilteringDTO.getMaxBirthYear(),
                foodMateFilteringDTO.getMinEntryYear(),
                foodMateFilteringDTO.getMaxEntryYear(),
                foodMateFilteringDTO.getMbti(),
                foodMateFilteringDTO.getColleges(),
                foodMateFilteringDTO.getMateTime(),
                foodMateFilteringDTO.getMenus(),
                foodMateFilteringDTO.getMealTime(),
                foodMateFilteringDTO.getDiningManner()
        );
        int size = 500; // 1차로 db에서 500명을 가져옴.
        Random random = new Random();
        //500개를 초과하면 랜덤으로 500명을 뽑음. 500개 이하이면 그대로 반환
        if(profilesByFilters.size() > size){
            int randomStartIndex = random.nextInt(profilesByFilters.size() - size);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + size);
        }
        return FoodMateProfileDTO.of(profilesByFilters);
    }
}
