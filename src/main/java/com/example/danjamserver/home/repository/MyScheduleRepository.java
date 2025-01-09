package com.example.danjamserver.home.repository;

import com.example.danjamserver.home.domain.MySchedule;
import com.example.danjamserver.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyScheduleRepository extends JpaRepository<MySchedule, Long> {
    // 유저의 모든 일정을 조회하는 메서드
    List<MySchedule> findSchedulesByUser(User user);
}
