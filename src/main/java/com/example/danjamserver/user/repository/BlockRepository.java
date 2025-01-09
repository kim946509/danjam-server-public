package com.example.danjamserver.user.repository;

import com.example.danjamserver.user.domain.Block;
import com.example.danjamserver.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("SELECT b.blocked FROM Block b WHERE b.blocker = :blocker AND b.isBlocked = true")
    List<User> findBlockedUsersByBlocker(@Param("blocker") User blocker);
    Optional<Block> findByBlockerAndBlocked(User blocker, User blocked);
}
