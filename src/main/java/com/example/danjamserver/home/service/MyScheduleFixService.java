package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.MySchedule;
import com.example.danjamserver.home.dto.MyScheduleInputDTO;
import com.example.danjamserver.home.repository.MyScheduleRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.ForbiddenAccessException;
import com.example.danjamserver.util.exception.InvalidInputException;
import com.example.danjamserver.util.exception.InvalidResourceException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MyScheduleFixService {

    private final ModelMapper modelMapper;
    private final MyScheduleRepository myScheduleRepository;

    @Transactional
    public void fixMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, Long scheduleId,
                              MyScheduleInputDTO myScheduleInputDTO) {

        // 유저 정보 조회 및 유효성 검사
        User user = customUserDetails.getUser();
        if (user == null) {
            throw new InvalidTokenUser();
        }

        // 스케쥴이 존재하지 않을 경우 예외 처리
        MySchedule mySchedule = myScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new InvalidResourceException(ResultCode.CAN_NOT_FIND_RESOURCE));

        // 유저의 스케쥴이 아닐 경우 예외 처리
        if (!mySchedule.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException();
        }

        //시작일자 > 종료일자
        if (myScheduleInputDTO.getStartDate().isAfter(myScheduleInputDTO.getEndDate())) {
            throw new InvalidInputException(ResultCode.INVALID_SCHEDULE_DATE);
        }
        // 스케쥴 수정
        modelMapper.map(myScheduleInputDTO, mySchedule);
        myScheduleRepository.save(mySchedule);
    }
}
