package com.example.danjamserver.user.dto;

import com.example.danjamserver.user.domain.Report;
import com.example.danjamserver.user.enums.ReportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponseDTO {
    private String comment;
    private ReportType reportType;
    private Long reporterUserId;

    public static ReportResponseDTO from(Report report) {
        return ReportResponseDTO.builder()
                .comment(report.getComment())
                .reportType(report.getReportType())
                .reporterUserId(report.getReporterUserId())
                .build();
    }
}
