package com.example.danjamserver.user.domain;

import com.example.danjamserver.user.dto.ReportDTO;
import com.example.danjamserver.user.enums.ReportType;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "report")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reporterUserId; // 신고한 사용자의 Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    // 리포트 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    // 빌더 메서드
    public static Report of(ReportDTO reportDTO, Long reporterUserId, User targetUser) {
        return Report.builder()
                .reporterUserId(reporterUserId)
                .user(targetUser)
                .comment(reportDTO.getComment())
                .reportType(reportDTO.getReportType())
                .build();
    }

}
