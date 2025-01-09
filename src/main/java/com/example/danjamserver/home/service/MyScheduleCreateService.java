package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.MySchedule;
import com.example.danjamserver.home.dto.MyScheduleInputDTO;
import com.example.danjamserver.home.repository.MyScheduleRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
/**
 * MySchedule을 위한 Service
 * createSchedule : 스케쥴 생성
 */
public class MyScheduleCreateService {

    private final ModelMapper modelMapper;
    private final MyScheduleRepository myScheduleRepository;

    /**
     * 스케쥴 생성
     * @param customUserDetails : 유저 컨텍스트 정보
     * @param myScheduleInputDTO : 스케쥴 생성 DTO
     * @return RestResponse<String>
     */
    @Transactional
    public void createMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, MyScheduleInputDTO myScheduleInputDTO) {

        User user = customUserDetails.getUser();
        if(user == null) {
            throw new InvalidTokenUser();
        }

        //시작일자 > 종료일자
        if(myScheduleInputDTO.getStartDate().isAfter(myScheduleInputDTO.getEndDate())) {
            throw new InvalidInputException(ResultCode.INVALID_SCHEDULE_DATE);
        }

        MySchedule mySchedule = modelMapper.map(myScheduleInputDTO, MySchedule.class);
        mySchedule.setUser(user);
        myScheduleRepository.save(mySchedule);
    }
}
