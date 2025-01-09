package com.example.danjamserver.chatroom.service;

import com.example.danjamserver.chatroom.dto.requests.UserSearchReq;
import com.example.danjamserver.chatroom.dto.responses.UserSearchRes;
import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.roomMate.domain.RoomMateProfile;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.BlockRepository;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.walkMate.domain.WalkMateProfile;
import com.example.danjamserver.workoutMate.domain.WorkoutMateProfile;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public List<UserSearchRes> searchUser(UserSearchReq req, User user) {
        MateType mateType = req.getSearchingMateType();
        School school = user.getSchool();

        // 같은 학교에 속한 모든 사용자들을 조회
        List<User> users = userRepository.findAllBySchool(school);

        // 차단된 유저 ID 리스트 조회
        Set<Long> blockedUserIds = blockRepository.findBlockedUsersByBlocker(user)
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        // MateType에 따라 특정 프로필 클래스를 결정
        Class<? extends MateProfile> profileClass = resolveProfileClass(mateType);

        // 각 유저에 대해 hasSpecificMateProfile 적용하여 필터링
        List<User> filteredUsers = users.stream()
                .filter(u -> !blockedUserIds.contains(u.getId())) // 차단된 유저 제외
                .filter(u -> hasSpecificMateProfile(u, profileClass))
                .filter(u -> {
                    if (mateType == MateType.ROOMMATE) {
                        // RoomMate일 경우 추가 필터링 로직 (예: 같은 성별 필터링)
                        return u.getGender().equals(user.getGender());
                    }
                    return true;
                })
                .toList();

        // 필터링된 유저 리스트를 UserSearchRes 리스트로 변환하여 반환
        return filteredUsers.stream()
                .map(UserSearchRes::create)  // User 객체를 UserSearchRes로 변환
                .collect(Collectors.toList());
    }

    private boolean hasSpecificMateProfile(User user, Class<? extends MateProfile> profileClass) {
        return user.getMateProfiles().stream()
                .anyMatch(profileClass::isInstance);
    }

    // MateType에 따른 MateProfile 클래스 결정
    private Class<? extends MateProfile> resolveProfileClass(MateType mateType) {
        switch (mateType) {
            case ROOMMATE:
                return RoomMateProfile.class;
            case FOODMATE:
                return FoodMateProfile.class;
            case WORKOUTMATE:
                return WorkoutMateProfile.class;
            case WALKMATE:
                return WalkMateProfile.class;
            case STUDYMATE:
                return StudyMateProfile.class;
            default:
                throw new IllegalArgumentException("Unsupported MateType: " + mateType);
        }
    }
}
