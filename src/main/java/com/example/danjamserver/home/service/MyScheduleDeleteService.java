package com.example.danjamserver.home.service;

import com.example.danjamserver.home.domain.MySchedule;
import com.example.danjamserver.home.repository.MyScheduleRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.exception.ForbiddenAccessException;
import com.example.danjamserver.util.exception.InvalidResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MyScheduleDeleteService {

    private final MyScheduleRepository myScheduleRepository;

    @Transactional
    public void deleteMySchedule(@AuthenticationPrincipal CustomUserDetails customUserDetails, Long scheduleId) {

        User user = customUserDetails.getUser();
        MySchedule mySchedule = myScheduleRepository.findById(scheduleId).orElseThrow(InvalidResourceException::new);

        if (!mySchedule.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException();
        }

        myScheduleRepository.delete(mySchedule);
    }
}
