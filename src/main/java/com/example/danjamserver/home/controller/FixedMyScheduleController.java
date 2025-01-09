package com.example.danjamserver.home.controller;

import com.example.danjamserver.home.dto.FixedMyScheduleDTO;
import com.example.danjamserver.home.dto.FixedMyScheduleListDTO;
import com.example.danjamserver.home.service.FixedMyScheduleSetService;
import com.example.danjamserver.home.service.FixedMyScheduleViewService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class FixedMyScheduleController {

    private final FixedMyScheduleSetService fixedMyScheduleSetService;
    private final FixedMyScheduleViewService fixedMyScheduleViewService;

    @PostMapping("/fixed-myschedule")
    public ResponseEntity<ApiResponseMessage> setFixedMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid FixedMyScheduleListDTO fixedMyScheduleDTOList){

        fixedMyScheduleSetService.setFixedMySchedule(customUserDetails, fixedMyScheduleDTOList);
            return ResponseEntity.ok(ApiResponseMessage.of("고정 스케쥴 등록 성공"));
    }

    @GetMapping("/fixed-myschedule")
    public ResponseEntity<ApiResponseData<List<FixedMyScheduleDTO>>> getFixedMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails){
            //서비스 호출
            List<FixedMyScheduleDTO> fixedMySchedule = fixedMyScheduleViewService.getFixedMySchedule(customUserDetails);
            return ResponseEntity.ok(ApiResponseData.of(fixedMySchedule, "고정 스케쥴 조회 성공"));
    }
}
