package com.example.danjamserver.user.dto;

import com.example.danjamserver.user.domain.Block;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDTO {
    String blockerNickname;
    List<String> blockedNickname;

    public static BlockResponseDTO of(Block block, List<String> BlockedNicknames) {
        return new BlockResponseDTO(
                block.getBlocker().getNickname(), BlockedNicknames
        );
    }
}
