package com.example.danjamserver.user.dto;

import com.example.danjamserver.user.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportDTO {

    @NotNull(message = "신고하는 유저의 닉네임을 입력해주세요.")
    @Size(min = 2, max = 32, message = "닉네임은 2자 이상, 32자 이내여야 합니다.")
    private String nickname;

    @NotBlank(message = "신고 내역을 입력해주세요.")
    @Size(min = 2, max = 50, message = "신고내역은 2자 이상, 50자 이내여야 합니다.")
    private String comment;

    @NotNull(message = "신고 유형을 선택해주세요.")
    private ReportType reportType;
}
