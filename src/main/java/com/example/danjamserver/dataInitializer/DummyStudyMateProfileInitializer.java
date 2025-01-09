package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.domain.UserSubject;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import com.example.danjamserver.studyMate.repository.PreferredStudyTypeRepository;
import com.example.danjamserver.studyMate.repository.StudyMateProfileRepository;
import com.example.danjamserver.studyMate.repository.UserSubjectRepository;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Order(3)
public class DummyStudyMateProfileInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final StudyMateProfileRepository studyMateProfileRepository;
    private final PreferredStudyTypeRepository preferredStudyTypeRepository;
    private final UserSubjectRepository userSubjectRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 더미 유저가 존재하는지 확인. 존재하지 않으면 메이트 프로필 생성하지 않음
        String checkUsername = "test" + DummyUsersInitializer.startDummyUserNumber;
        User checkUser = userRepository.findByUsername(checkUsername).orElse(null);
        if (checkUser == null) {
            return;
        }

        // 이미 더미 유저의 스터디 메이트 프로필이 존재하는지 확인. 존재하면 더미 스터디 메이트 프로필 생성하지 않음
        StudyMateProfile checkStudyMateProfile = studyMateProfileRepository.findByUserId(checkUser.getId()).orElse(null);
        if (checkStudyMateProfile != null) {
            return;
        }

        // 더미 스터디 메이트 프로필 생성
        String dummyUsername = "test";
        for (int i = DummyUsersInitializer.startDummyUserNumber; i <= DummyUsersInitializer.endDummyUserNumber; i++) {
            User user = userRepository.findByUsername(dummyUsername + i).orElse(null);
            if (user == null) {
                continue;
            }

            // 이미 해당 더미유저의 StudyMateProfile이 존재하는지 확인
            StudyMateProfile checkUserStudyMateProfile = studyMateProfileRepository.findByUserId(user.getId()).orElse(null);
            if (checkUserStudyMateProfile != null) {
                continue;
            }

            // StudyMateProfile 생성 및 저장
            StudyMateProfile studyMateProfile = setRandomStudyMateProfile(user);

            // PreferredStudyType 저장
            setRandomStudyTypes(studyMateProfile);

            // UserSubject 저장
            setRandomSubjects(studyMateProfile);

        }

    }

    // StudyMateProfile 저장
    private StudyMateProfile setRandomStudyMateProfile(User user) {
        StudyMateProfile studyMateProfile = StudyMateProfile.builder()
                .user(user)
                .preferredStudyTime(getRandomStudyTime())
                .averageGrade(getRandomAverageGrade())
                .mateType(MateType.STUDYMATE)
                .build();
        studyMateProfileRepository.save(studyMateProfile);
        return studyMateProfile;
    }

    // PreferredStudyType 저장
    private void setRandomStudyTypes(StudyMateProfile studyMateProfile) {
        List<StudyType> randomStudyTypes = getRandomStudyTypes();
        for (StudyType studyType : randomStudyTypes) {
            preferredStudyTypeRepository.save(PreferredStudyType.builder()
                    .studyMateProfile(studyMateProfile)
                    .studyType(studyType)
                    .build());
        }
    }

    // UserSubject 저장
    private void setRandomSubjects(StudyMateProfile studyMateProfile) {
        List<String> randomSubjects = getRandomSubjects().orElse(null);
        if(randomSubjects == null) {
            return;
        }
        for (String subject : randomSubjects) {
            userSubjectRepository.save(UserSubject.builder()
                    .studyMateProfile(studyMateProfile)
                    .subject(subject)
                    .build());
        }
    }


    private StudyTime getRandomStudyTime() {
        StudyTime[] studyTimes = StudyTime.values();
        return studyTimes[random.nextInt(studyTimes.length)];
    }

    private AverageGrade getRandomAverageGrade() {
        AverageGrade[] averageGrades = AverageGrade.values();
        return averageGrades[random.nextInt(averageGrades.length)];
    }

    private List<StudyType> getRandomStudyTypes() {
        List<StudyType> studyTypes = new ArrayList<>(Arrays.stream(StudyType.values()).toList());
        // 랜덤으로 1~8개의 스터디 타입을 선택
        int randomStudyTypeCount = random.nextInt(8) + 1;
        List<StudyType> randomStudyTypes = new ArrayList<>();
        for (int i = 0; i < randomStudyTypeCount; i++) {
            int randomIndex = random.nextInt(studyTypes.size());
            randomStudyTypes.add(studyTypes.get(randomIndex));
            studyTypes.removeIf(randomStudyTypes::contains);
        }
        return randomStudyTypes;
    }

    private Optional<List<String>> getRandomSubjects() {
        List<String> subjects = new ArrayList<>(Arrays.asList("수학", "토익", "파이썬", "미적분", "화학", "정보처리기사", "공무원시험", "경제"));
        // 랜덤으로 0~8개의 과목을 선택
        int randomSubjectCount = random.nextInt(8);
        List<String> randomSubjects = new ArrayList<>();
        for (int i = 0; i < randomSubjectCount; i++) {
            int randomIndex = random.nextInt(subjects.size());
            randomSubjects.add(subjects.get(randomIndex));
            subjects.removeIf(randomSubjects::contains);
        }
        return Optional.of(randomSubjects);
    }
}
