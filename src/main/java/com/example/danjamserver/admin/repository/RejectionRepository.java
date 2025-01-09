package com.example.danjamserver.admin.repository;

import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.admin.domain.RejectionReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RejectionRepository extends JpaRepository<RejectionReason, Long> {
    @Query("SELECT DISTINCT r.user FROM RejectionReason r")
    List<User> findUsersWithRejection();
}
