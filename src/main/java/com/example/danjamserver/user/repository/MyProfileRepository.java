package com.example.danjamserver.user.repository;

import com.example.danjamserver.user.domain.MyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MyProfileRepository extends JpaRepository<MyProfile, Long> {
    Optional<MyProfile> findByUserId(Long userId);

    @Modifying
    @Query("update MyProfile mp set mp.profileImgUrl = :newProfileImgUrl where mp.id = :id")
    void updateProfileImgUrl(Long id, String newProfileImgUrl);
}