package com.example.danjamserver.studyMate.dto;

import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.domain.UserSubject;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyMateProfileInputDTO {

    @NotEmpty(message = "희망하는 스터디를 선택해주세요.")
    private Set<StudyType> preferredStudyTypes;

    @NotNull(message = "희망하는 스터디 소요 시간을 선택해주세요.")
    private StudyTime preferredStudyTime;

    private Set<String> subjects;

    @NotNull(message = "평균 학점을 선택해주세요.")
    private AverageGrade averageGrade;

    public static StudyMateProfileInputDTO from(StudyMateProfile studyMateProfile) {
        StudyMateProfileInputDTO profile = new StudyMateProfileInputDTO();

        profile.setPreferredStudyTypes(studyMateProfile.getPreferredStudyTypes().stream()
                .map(PreferredStudyType::getStudyType)
                .collect(Collectors.toSet()));

        profile.setPreferredStudyTime(studyMateProfile.getPreferredStudyTime());

        profile.setSubjects(studyMateProfile.getUserSubjects().stream()
                .map(UserSubject::getSubject).collect(Collectors.toSet()));

        profile.setAverageGrade(studyMateProfile.getAverageGrade());

        return profile;
    }
}
