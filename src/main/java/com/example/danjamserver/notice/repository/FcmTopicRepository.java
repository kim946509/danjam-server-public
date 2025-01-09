package com.example.danjamserver.notice.repository;

import com.example.danjamserver.notice.domain.FcmTopic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FcmTopicRepository extends JpaRepository<FcmTopic, Long> {
    Optional<FcmTopic> findByName(String topicName);

    @Query("SELECT t.name FROM FcmTopic t WHERE t.id = :id")
    Optional<String> findTopicNameById(@Param("id") Long id);

}
