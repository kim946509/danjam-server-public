package com.example.danjamserver.notice.repository;

import com.example.danjamserver.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Notice findByUserId(Long userId);

    @Query("SELECT n.token FROM Notice n WHERE n.user.id = :userId")
    String findTokenByUserId(@Param("userId") Long userId);

}
