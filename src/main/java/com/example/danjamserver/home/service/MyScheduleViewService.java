package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.MySchedule;
import com.example.danjamserver.home.dto.MyScheduleResponseDTO;
import com.example.danjamserver.home.repository.MyScheduleRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * MySchedule 조회를 위한 Service
 * getMySchedules : 스케쥴 전체 조회
 */
public class MyScheduleViewService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MyScheduleRepository myScheduleRepository;

    /**
     * @param customUserDetails : 유저 컨텍스트 정보
     * @return RestResponse<ArrayList < MyScheduleResponseDTO>> : MyScheduleResponseDTO 리스트
     */
    @Transactional(readOnly = true)
    public List<MyScheduleResponseDTO> getMySchedules(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();
        // 유저 정보 조회 및 유효성 검사
        if (user == null) {
            throw new InvalidTokenUser();
        }

        // 유저의 모든 일정 조회
        List<MySchedule> mySchedules = myScheduleRepository.findSchedulesByUser(user);

        // MyScheduleResponseDTO 리스트 생성
        List<MyScheduleResponseDTO> mySchedulesResponseDTO = new ArrayList<>();

        // MySchedule을 MyScheduleResponseDTO로 변환
        for (MySchedule mySchedule : mySchedules) {
            MyScheduleResponseDTO myscheduleResponseDTO = modelMapper.map(mySchedule, MyScheduleResponseDTO.class);
            mySchedulesResponseDTO.add(myscheduleResponseDTO);
        }

        // MyScheduleResponseDTO 리스트 반환
        return mySchedulesResponseDTO;

    }


}
