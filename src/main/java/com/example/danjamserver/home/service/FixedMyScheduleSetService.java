package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.FixedMySchedule;
import com.example.danjamserver.home.dto.FixedMyScheduleDTO;
import com.example.danjamserver.home.dto.FixedMyScheduleListDTO;
import com.example.danjamserver.home.repository.FixedMyScheduleRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedMyScheduleSetService {
    private final FixedMyScheduleRepository fixedMyScheduleRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void setFixedMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, FixedMyScheduleListDTO fixedMyScheduleListDTO){
        User user = customUserDetails.getUser();
        if(user == null) {
            throw new InvalidTokenUser();
        }

        //기존에 유저의 고정시간표를 모두 삭제
        fixedMyScheduleRepository.deleteAllByUser(user);

        List<FixedMyScheduleDTO> fixedMyScheduleList = fixedMyScheduleListDTO.getFixedMyScheduleDTOList();

        //유저의 고정시간표 등록
        List<FixedMySchedule> fixedMySchedules = fixedMyScheduleList.stream()
                .map(dto -> {
                    if(dto.getStartTime().isAfter(dto.getEndTime())) {
                        //시작시간 > 종료시간
                        throw new InvalidInputException(ResultCode.INVALID_SCHEDULE_TIME);
                    }
                    FixedMySchedule fixedMySchedule = modelMapper.map(dto, FixedMySchedule.class);
                    fixedMySchedule.setUser(user);
                    return fixedMySchedule;
                }).toList();

        fixedMyScheduleRepository.saveAll(fixedMySchedules);

    }
}
