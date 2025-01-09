package com.example.danjamserver.mate.dto;

import com.example.danjamserver.mate.domain.MateType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MateLikeDTO {

    @NotNull(message = "메이트 타입을 지정해주세요")
    private MateType mateType;

    @NotNull(message = "상대방 닉네임을 입력해주세요.")
    private String nickname; //닉네임
}
