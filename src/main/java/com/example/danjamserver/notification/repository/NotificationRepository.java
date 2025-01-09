package com.example.danjamserver.notification.repository;

import com.example.danjamserver.notification.domain.Notification;
import com.example.danjamserver.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  @Modifying
  @Query("UPDATE Notification n SET n.status = :status, n.modifiedDateTime = CURRENT_TIMESTAMP " +
          "WHERE n.receiver.id = :userId AND n.id IN :ids AND " +
          "((:status = 'DONE' AND n.status = 'PENDING') OR (:status = 'DISMISSED' AND n.status != 'DISMISSED'))")
  int updateNotificationStatus(@Param("userId") Long userId, @Param("ids") List<Long> ids, @Param("status") NotificationStatus status);

  @Query("SELECT n FROM Notification n WHERE n.receiver.id = :userId AND  n.status != 'DISMISSED' ORDER BY n.createdDateTime DESC")
  List<Notification> findByReceiverId(@Param("userId") Long userId);
}
