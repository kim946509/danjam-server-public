package com.example.danjamserver.roomMate.controller;

import com.example.danjamserver.roomMate.dto.RoomMateCandidateDetailDTO;
import com.example.danjamserver.roomMate.dto.RoomMateFilteringDTO;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import com.example.danjamserver.roomMate.service.RoomMateCandidateService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mate")
public class RoomMateCandidateController {

    private final RoomMateCandidateService roomMateCandidateService;

    @GetMapping("/roommate/candidate")
    public ResponseEntity<ApiResponseData<List<RoomMateCandidateDetailDTO>>> getRoomMateCandidate(@AuthenticationPrincipal CustomUserDetails customUserDetails
            , @RequestParam(value = "mbti", required = false) String mbti
            , @RequestParam(value = "minBirthYear", required = false) String minBirthYear
            , @RequestParam(value = "maxBirthYear", required = false) String maxBirthYear
            , @RequestParam(value = "minEntryYear", required = false) String minEntryYear
            , @RequestParam(value = "maxEntryYear", required = false) String maxEntryYear
            , @RequestParam(value = "colleges", required = false) Set<String> colleges
            , @RequestParam(value = "hopeDormitories", required = false) Set<String> hopeDormitories
            , @RequestParam(value = "hopeRoomPersons", required = false) Set<Integer> hopeRoomPersons
            , @RequestParam(value = "isSmoking", required = false) Set<Boolean> isSmoking
            , @RequestParam(value = "hotLevel", required = false) Level hotLevel
            , @RequestParam(value = "coldLevel", required = false) Level coldLevel
            , @RequestParam(value = "activityTime", required = false) ActivityTime activityTime
            , @RequestParam(value = "cleanPeriod", required = false) CleanPeriod cleanPeriod
            , @RequestParam(value = "showerTime", required = false) ShowerTime showerTime
            , @RequestParam(value = "sleepHabits", required = false) Integer sleepHabits //중복 x
    ) {
        RoomMateFilteringDTO roomMateFilteringDTO = RoomMateFilteringDTO.builder()
                .mbti(mbti)
                .minBirthYear(minBirthYear)
                .maxBirthYear(maxBirthYear)
                .minEntryYear(minEntryYear)
                .maxEntryYear(maxEntryYear)
                .colleges(colleges)
                .hopeDormitories(hopeDormitories)
                .hopeRoomPersons(hopeRoomPersons)
                .isSmoking(isSmoking)
                .hotLevel(hotLevel)
                .coldLevel(coldLevel)
                .activityTime(activityTime)
                .cleanPeriod(cleanPeriod)
                .showerTime(showerTime)
                .sleepHabits(sleepHabits)
                .build();


        List<RoomMateCandidateDetailDTO> roomMateCandidateList = roomMateCandidateService.getRoomMateCandidateList(customUserDetails, roomMateFilteringDTO);

        return ResponseEntity.ok(ApiResponseData.of(roomMateCandidateList, "룸메이트 후보 리스트 조회 성공"));
    }


}
