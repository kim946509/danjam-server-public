package com.example.danjamserver.chatroom.dto.requests;

import com.example.danjamserver.mate.domain.MateType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchReq {
    private MateType searchingMateType;

    @Builder
    public UserSearchReq(MateType searchingMateType) {
        this.searchingMateType = searchingMateType;
    }
}
