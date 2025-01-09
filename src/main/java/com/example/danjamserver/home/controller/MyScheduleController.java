package com.example.danjamserver.home.controller;

import com.example.danjamserver.home.dto.MyScheduleInputDTO;
import com.example.danjamserver.home.dto.MyScheduleResponseDTO;
import com.example.danjamserver.home.service.MyScheduleCreateService;
import com.example.danjamserver.home.service.MyScheduleDeleteService;
import com.example.danjamserver.home.service.MyScheduleFixService;
import com.example.danjamserver.home.service.MyScheduleViewService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.util.response.ApiResponseData;
import com.example.danjamserver.util.response.ApiResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
/**
 * MySchedule Controller
 * @param
 *    myScheduleCreateService : MySchedule 생성 Service
 *    myScheduleViewService : MySchedule 조회 Service
 *
 */
public class MyScheduleController {

    private final MyScheduleCreateService myScheduleCreateService;
    private final MyScheduleViewService myScheduleViewService;
    private final MyScheduleDeleteService myScheduleDeleteService;
    private final MyScheduleFixService myScheduleFixService;

    /**
     * 홈에서 내 개인 스케쥴을 생성할 때 사용하는 API
     * @param customUserDetails : 유저 컨텍스트 정보
     * @param myScheduleInputDTO : MySchedule 생성 DTO
     * @return : ResponseEntity<Map<String, String>>
     */
    @PostMapping("/myschedule")
    public ResponseEntity<ApiResponseMessage> createMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid MyScheduleInputDTO myScheduleInputDTO) {
        // MySchedule 생성
        myScheduleCreateService.createMySchedule(customUserDetails, myScheduleInputDTO);
        ApiResponseMessage response = ApiResponseMessage.of("MySchedule 생성 성공");
        return ResponseEntity.ok(response);
    }


    /**
     * 내 개인 스케쥴을 전체 조회할 때 사용하는 API
     * @param customUserDetails : 유저 컨텍스트 정보
     * @return : MyScheduleResponseDTO 리스트
     */
    @GetMapping("/myschedule")
    public ResponseEntity<ApiResponseData<List<MyScheduleResponseDTO>>> getMySchedules(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // MySchedule 전체 조회
        List<MyScheduleResponseDTO> mySchedules = myScheduleViewService.getMySchedules(customUserDetails);
        ApiResponseData<List<MyScheduleResponseDTO>> response = ApiResponseData.of(mySchedules, "MySchedule 전체 조회 성공");
        return ResponseEntity.ok(response);
    }

    /**
     * 내 개인 스케쥴을 삭제할 때 사용하는 API
     * @param customUserDetails : 유저 컨텍스트 정보
     * @param myScheduleId : 삭제할 MySchedule ID
     * @return : ResponseEntity<Map<String, String>>
     **/
    @DeleteMapping("/myschedule/{myScheduleId}")
    public ResponseEntity<ApiResponseMessage> deleteMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long myScheduleId){
            // MySchedule 삭제
            myScheduleDeleteService.deleteMySchedule(customUserDetails, myScheduleId);
            return ResponseEntity.ok(ApiResponseMessage.of("MySchedule 삭제 성공"));
    }

    /**
     * 내 개인 스케쥴을 수정할 때 사용하는 API
     * @param customUserDetails : 유저 컨텍스트 정보
     * @param myScheduleInputDTO : 수정할 MySchedule DTO
     * @param myScheduleId : 수정할 MySchedule ID
     * @return
     */
    @PutMapping("/myschedule/{myScheduleId}")
    public ResponseEntity<ApiResponseMessage> fixMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid MyScheduleInputDTO myScheduleInputDTO, @PathVariable Long myScheduleId){
        myScheduleFixService.fixMySchedule(customUserDetails, myScheduleId, myScheduleInputDTO);
        return ResponseEntity.ok(ApiResponseMessage.of("MySchedule 수정 성공"));
    }

}
