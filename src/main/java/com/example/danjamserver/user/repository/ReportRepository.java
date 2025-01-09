package com.example.danjamserver.user.repository;

import com.example.danjamserver.user.domain.Report;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.enums.ReportType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Boolean existsByReporterUserIdAndUserAndReportType(Long reporterUserId, User user, ReportType reportType);
    @Query("SELECT r FROM Report r WHERE r.user.id = :userId")
    List<Report> findByUserId(@Param("userId") Long userId);
}
