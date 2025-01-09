package com.example.danjamserver.mate.service;

import com.example.danjamserver.mate.domain.MateLike;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.mate.dto.MateLikeDTO;
import com.example.danjamserver.mate.dto.MateLikeResponseDTO;
import com.example.danjamserver.mate.repository.MateLikeRepository;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.InvalidTokenUser;
import com.example.danjamserver.util.exception.ResultCode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MateLikeService {

    private final MateLikeRepository mateLikeRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public Map<MateType, List<MateLikeResponseDTO>> readMateLikesByUser(CustomUserDetails customUserDetails) {
        User user = findUserByUsername(customUserDetails.getUsername());
        List<MateLike> mateLikes = mateLikeRepository.findByUser(user);

        return mateLikes.stream()
                .sorted(Comparator.comparing(MateLike::getModifiedDateTime).reversed())
                .map(MateLikeResponseDTO::from)
                .collect(Collectors.groupingBy(MateLikeResponseDTO::getMateType));
    }

    @Transactional
    public void saveMateLike(MateLikeDTO mateLikeDTO, CustomUserDetails customUserDetails) {
        User user = findUserByUsername(customUserDetails.getUsername());
        User targetUser = findUserByNickname(mateLikeDTO.getNickname());

        validateSelfLike(user, targetUser);

        if (!mateLikeExists(user, targetUser, mateLikeDTO.getMateType())) {
            MateLike mateLike = MateLike.builder()
                    .user(user)
                    .targetUser(targetUser)
                    .mateType(mateLikeDTO.getMateType())
                    .build();
            mateLikeRepository.save(mateLike);
        } else {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "이미 찜한 유저입니다.");
        }
    }

    // 메이트 찜 삭제
    @Transactional
    public void deleteMateLike(MateLikeDTO mateLikeDTO, CustomUserDetails customUserDetails) {
        User user = findUserByUsername(customUserDetails.getUsername());
        User targetUser = findUserByNickname(mateLikeDTO.getNickname());

        validateSelfLike(user, targetUser);

        if (mateLikeExists(user, targetUser, mateLikeDTO.getMateType())) {
            mateLikeRepository.deleteByUserIdAndTargetUserIdAndMateType(user.getId(), targetUser.getId(),
                    mateLikeDTO.getMateType());
        } else {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "찜 목록을 찾을 수 없습니다.");
        }
    }

    // 사용자 조회
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(InvalidTokenUser::new);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(InvalidTokenUser::new);
    }

    // 자기 자신을 좋아요하는 경우
    private void validateSelfLike(User user, User targetUser) {
        if (user.getUsername().equals(targetUser.getUsername())) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, "자기 자신을 찜 할 수 없습니다.");
        }
    }

    // MateLike 존재 여부 확인
    private boolean mateLikeExists(User user, User targetUser, MateType mateType) {
        return mateLikeRepository.checkIfMateLikeExists(user, targetUser, mateType);
    }
}

