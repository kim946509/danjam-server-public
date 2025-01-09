package com.example.danjamserver.roomMate.service;

import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.roomMate.dto.RoomMateFilteringDTO;
import com.example.danjamserver.roomMate.dto.RoomMateProfileDTO;
import com.example.danjamserver.roomMate.repository.RoomMateProfileRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomMateFilteringService {

    @Autowired
    RoomMateProfileRepository roomMateProfileRepository;

    @Cacheable(
            key = "#schoolId + '_' + #gender + '_' + #roomMateFilteringDTO.mbti + '_' + " +
                    "(#roomMateFilteringDTO.minBirthYear ?: '') + '_' + (#roomMateFilteringDTO.maxBirthYear ?: '') + '_' + "
                    +
                    "(#roomMateFilteringDTO.minEntryYear ?: '') + '_' + (#roomMateFilteringDTO.maxEntryYear ?: '') + '_' + "
                    +
                    "(#roomMateFilteringDTO.colleges ?: '') + '_' + (#isSmoking ?: '') + '_' + " +
                    "(#roomMateFilteringDTO.hotLevel ?: '') + '_' + (#roomMateFilteringDTO.coldLevel ?: '') + '_' + " +
                    "(#roomMateFilteringDTO.activityTime ?: '') + '_' + (#roomMateFilteringDTO.cleanPeriod ?: '') + '_' + "
                    +
                    "(#roomMateFilteringDTO.sleepHabits ?: '') + '_' + (#roomMateFilteringDTO.showerTime ?: '') + '_' + "
                    +
                    "(#roomMateFilteringDTO.hopeDormitories ?: '') + '_' + (#roomMateFilteringDTO.hopeRoomPersons ?: '')",
            value = "roomMateCandidateList"
    )
    public List<RoomMateProfileDTO> getFilteredCandidates(RoomMateFilteringDTO roomMateFilteringDTO, Long schoolId,
                                                          Integer gender, Boolean isSmoking) {
        List<RoomMateProfile> profilesByFilters = roomMateProfileRepository.findProfilesByFilters(
                schoolId,
                roomMateFilteringDTO.getMbti(),
                gender,
                roomMateFilteringDTO.getMinBirthYear(),
                roomMateFilteringDTO.getMaxBirthYear(),
                roomMateFilteringDTO.getMinEntryYear(),
                roomMateFilteringDTO.getMaxEntryYear(),
                roomMateFilteringDTO.getColleges(),
                isSmoking,
                roomMateFilteringDTO.getHotLevel(),
                roomMateFilteringDTO.getColdLevel(),
                roomMateFilteringDTO.getActivityTime(),
                roomMateFilteringDTO.getCleanPeriod(),
                roomMateFilteringDTO.getSleepHabits(),
                roomMateFilteringDTO.getShowerTime(),
                roomMateFilteringDTO.getHopeDormitories(),
                roomMateFilteringDTO.getHopeRoomPersons()
        );
        int size = 500; // 1차로 db에서 500명을 가져옴.
        Random random = new Random();
        //size를 초과하면 랜덤으로 size명 뽑음. size개 이하이면 그대로 반환
        if (profilesByFilters.size() > size) {
            int randomStartIndex = random.nextInt(profilesByFilters.size() - size);
            profilesByFilters = profilesByFilters.subList(randomStartIndex, randomStartIndex + size);
        }
        return RoomMateProfileDTO.of(profilesByFilters);
    }
}