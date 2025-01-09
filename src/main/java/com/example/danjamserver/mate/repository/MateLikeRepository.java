package com.example.danjamserver.mate.repository;

import com.example.danjamserver.mate.domain.MateLike;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MateLikeRepository extends JpaRepository<MateLike, Long> {
    List<MateLike> findByUser(User user);
    @Query("SELECT COUNT(m) > 0 FROM MateLike m WHERE m.user = :user AND m.targetUser = :targetUser AND m.mateType = :mateType")
    Boolean checkIfMateLikeExists(@Param("user") User user,
                                  @Param("targetUser") User targetUser,
                                  @Param("mateType") MateType mateType);
    // Custom delete query
    @Modifying
    @Query("DELETE FROM MateLike m WHERE m.user.id = :userId AND m.targetUser.id = :targetUserId AND m.mateType = :mateType")
    void deleteByUserIdAndTargetUserIdAndMateType(@Param("userId") Long userId,
                                                  @Param("targetUserId") Long targetUserId,
                                                  @Param("mateType") MateType mateType);
}
