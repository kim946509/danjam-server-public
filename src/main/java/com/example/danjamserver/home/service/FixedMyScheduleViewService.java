package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.FixedMySchedule;
import com.example.danjamserver.home.dto.FixedMyScheduleDTO;
import com.example.danjamserver.home.repository.FixedMyScheduleRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FixedMyScheduleViewService {
    private final ModelMapper modelMapper;
    private final FixedMyScheduleRepository fixedMyScheduleRepository;

    public List<FixedMyScheduleDTO> getFixedMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();
        // 유저 정보 조회 및 유효성 검사
        if (user == null) {
            throw new InvalidTokenUser();
        }

        //유저의 모든 고정 스케쥴 조회
        List<FixedMySchedule> fixedMySchedules = fixedMyScheduleRepository.getAllByUser(user);

        //DTO 로 변환
        List<FixedMyScheduleDTO> fixedMyScheduleDTOList = fixedMySchedules.stream()
                .map(fixedMySchedule -> modelMapper.map(fixedMySchedule, FixedMyScheduleDTO.class))
                .toList();

        return fixedMyScheduleDTOList;
    }
}
