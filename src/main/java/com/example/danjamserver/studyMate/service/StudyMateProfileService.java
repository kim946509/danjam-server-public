package com.example.danjamserver.studyMate.service;

import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.domain.UserSubject;
import com.example.danjamserver.studyMate.dto.StudyMateProfileInputDTO;
import com.example.danjamserver.studyMate.enums.StudyType;
import com.example.danjamserver.studyMate.repository.PreferredStudyTypeRepository;
import com.example.danjamserver.studyMate.repository.StudyMateProfileRepository;
import com.example.danjamserver.studyMate.repository.UserSubjectRepository;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.CustomValidationException;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.ResultCode;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyMateProfileService {

    private final PreferredStudyTypeRepository preferredStudyTypeRepository;
    private final UserSubjectRepository userSubjectRepository;
    private final StudyMateProfileRepository studyMateProfileRepository;
    private final MyProfileRepository myProfileRepository;

    @Transactional
    public void createStudyMateProfile(CustomUserDetails customUserDetails,
                                       StudyMateProfileInputDTO studyMateProfileInputDTO) {

        User user = customUserDetails.getUser();

        //이미 MyProfile이 존재하는지 확인
        if (!isMyProfileAlreadySetting(user)) {
            throw new InvalidRequestException(ResultCode.MYPROFILE_REQUIRED);
        }

        //이미 StudyMateProfile이 존재하는지 확인
        StudyMateProfile studyMateProfileCheck = studyMateProfileRepository.findByUserId(user.getId()).orElse(null);
        if (studyMateProfileCheck != null) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }

        // validation check
        String validationMessage = validateProfileInputDTO(studyMateProfileInputDTO);
        if (!validationMessage.isEmpty()) {
            throw new CustomValidationException(ResultCode.VALIDATION_ERROR, validationMessage);
        }

        // studyMateProfile 일반 선택 저장
        // studyMateProfileInputDTO의 정보를 StudyMateProfile에 변환하여 저장
        StudyMateProfile studyMateProfile = new StudyMateProfile();
        studyMateProfile.setPreferredStudyTime(studyMateProfileInputDTO.getPreferredStudyTime()); //선호하는 스터디 소요 시간 저장
        studyMateProfile.setAverageGrade(studyMateProfileInputDTO.getAverageGrade());//성적 정보 저장
        studyMateProfile.setUser(user); //유저 정보 저장
        studyMateProfile.setMateType(MateType.STUDYMATE); //MateType 저장
        studyMateProfileRepository.save(studyMateProfile);

        //일대다 관계(중복선택) 항목들 저장
        //선호하는 스터디 타입 저장
        Set<StudyType> preferredStudyTypes = studyMateProfileInputDTO.getPreferredStudyTypes();
        for (StudyType preferredStudyType : preferredStudyTypes) {
            preferredStudyTypeRepository.save(PreferredStudyType.builder()
                    .studyMateProfile(studyMateProfile)
                    .studyType(preferredStudyType)
                    .build());
        }

        //수강중인 과목 저장

        if (studyMateProfileInputDTO.getSubjects() != null && !studyMateProfileInputDTO.getSubjects().isEmpty()) {
            Set<String> userSubjects = studyMateProfileInputDTO.getSubjects();
            for (String userSubject : userSubjects) {
                userSubjectRepository.save(UserSubject.builder()
                        .studyMateProfile(studyMateProfile)
                        .subject(userSubject)
                        .build());
            }
        }
    }

    private String validateProfileInputDTO(StudyMateProfileInputDTO studyMateProfileInputDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        if (studyMateProfileInputDTO.getSubjects() != null && !(studyMateProfileInputDTO.getSubjects().isEmpty())) {
            validateSubjectLength(studyMateProfileInputDTO.getSubjects(), stringBuilder);
        }
        return stringBuilder.toString();
    }

    private void validateSubjectLength(Set<String> subjects, StringBuilder stringBuilder) {

        if (subjects.isEmpty()) {
            return;
        }

        for (String subject : subjects) {
            if (subject.length() > 20) {
                stringBuilder.append("수강과목은 20자 이하로 작성해주세요.");
                return;
            }
        }
    }

    public boolean isMyProfileAlreadySetting(User user) {
        boolean isAlreadySetting = true;
        MyProfile myProfile = myProfileRepository.findByUserId(user.getId()).orElse(null);
        if (myProfile == null || myProfile.getMbti() == null) {
            isAlreadySetting = false;
        }
        return isAlreadySetting;
    }
}
