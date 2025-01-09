package com.example.danjamserver.notification.dto;

import com.example.danjamserver.notification.enums.NotificationStatus;
import com.example.danjamserver.notification.enums.NotificationType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private List<String> receiverNames;
    private NotificationType type;
    private NotificationStatus status;
    private String title;
    private String content;
    private String url;
    private String fromName;
}
