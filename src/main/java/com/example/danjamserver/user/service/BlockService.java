package com.example.danjamserver.user.service;

import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.Block;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.dto.BlockDTO;
import com.example.danjamserver.user.dto.BlockResponseDTO;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Transactional
    public String toggleBlockStatus(CustomUserDetails customUserDetails, BlockDTO blockDTO) {
        try {
            String username = customUserDetails.getUsername();

            User blocker = userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);
            User blocked = userRepository.findByNickname(blockDTO.getNickname()).orElseThrow(InvalidTokenUser::new);

            if (blocker.getUsername().equals(blocked.getUsername())) {
                return "자기 자신을 차단할 수 없습니다.";
            }

            // 차단 상태 확인
            Block existingBlock = blockRepository.findByBlockerAndBlocked(blocker, blocked).orElse(null);

            if (existingBlock != null) {
                //상태 반전
                boolean newBlockStatus = !existingBlock.getIsBlocked();
                existingBlock.changeBlockedState(newBlockStatus);
                blockRepository.save(existingBlock);

                return newBlockStatus ? "유저가 차단되었습니다." : "유저가 차단 해제되었습니다.";
            } else {
                // 차단 이력이 없다면 새로운 차단 생성
                Block block = Block.builder()
                        .blocker(blocker)
                        .blocked(blocked)
                        .isBlocked(true)
                        .build();
                blockRepository.save(block);
                return "유저가 차단되었습니다.";
            }
        } catch (DataAccessException e) {
            return "데이터 베이스 오류가 발생하였습니다.";
        }
    }

    public List<BlockResponseDTO> getBlockedUsersByBlocker() {
        List<Block> blocks = blockRepository.findAll().stream()
                .sorted(Comparator.comparing(Block::getModifiedDateTime).reversed())
                .collect(Collectors.toList());

        Map<String, List<Block>> map = blocks.stream().
                collect(Collectors.groupingBy(block -> block.getBlocker().getNickname()));

        return map.entrySet().stream()
                .map(entry -> {
                    List<String> blockedNicknames = entry.getValue().stream()
                            .map(block -> block.getBlocked().getNickname())
                            .collect(Collectors.toList());
                    return BlockResponseDTO.of(entry.getValue().get(0), blockedNicknames);
                }).collect(Collectors.toList());
    }
}
