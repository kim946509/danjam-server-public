package com.example.danjamserver.home.repository;

import com.example.danjamserver.home.domain.FixedMySchedule;
import com.example.danjamserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixedMyScheduleRepository extends JpaRepository<FixedMySchedule,Long> {
    //유저의 모든 고정일정 삭제
    void deleteAllByUser(User user);

    //유저의 모든 고정일정 조회
    List<FixedMySchedule> getAllByUser(User user);
}
