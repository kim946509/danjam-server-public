package com.example.danjamserver.user.service;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.Report;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.dto.ReportDTO;
import com.example.danjamserver.user.dto.ReportResponseDTO;
import com.example.danjamserver.user.repository.ReportRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.danjamserver.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createReport(ReportDTO reportDTO, CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUsername();
        // 유저가 없다면 40201 에러 발생
        User user = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);
        User targetUser = userRepository.findByNickname(reportDTO.getNickname()).orElseThrow(() -> new UserNotFoundException("해당 닉네임을 가진 유저가 존재하지 않습니다."));

        Boolean exists = reportRepository.existsByReporterUserIdAndUserAndReportType(
                user.getId(), targetUser, reportDTO.getReportType());

        if (exists) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_REPORT); // 40301 BAD_REQUEST 신고내역 존재
        }

        reportRepository.save(Report.of(reportDTO, user.getId(), targetUser));
    }

    /**
     * 모든 유저 신고 내역 조회
     *
     * @return
     */
    public Map<String, List<ReportResponseDTO>> readReport() {
        List<Report> reports = reportRepository.findAll(); // 모든 신고 내역을 가져옵니다.
        return reports.stream()
                .sorted(Comparator.comparing(Report::getModifiedDateTime).reversed())
                .collect(Collectors.groupingBy(report -> report.getUser().getNickname(),
                        Collectors.mapping(ReportResponseDTO::from, Collectors.toList())));
    }


    /**
     * 특정 유저 신고 내역 조회
     *
     * @param username
     * @return
     */
    public Map<String, List<ReportResponseDTO>> readReport(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("해당 이름의 유저가 존재하지 않습니다."));

        List<Report> reports = reportRepository.findByUserId(user.getId());

        return reports.stream()
                .sorted(Comparator.comparing(Report::getModifiedDateTime).reversed())
                .collect(Collectors.groupingBy(report -> report.getUser().getNickname(),
                        Collectors.mapping(ReportResponseDTO::from, Collectors.toList())));
    }
}
