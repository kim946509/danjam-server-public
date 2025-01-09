package com.example.danjamserver.studyMate.dto;


import com.example.danjamserver.studyMate.domain.PreferredStudyType;
import com.example.danjamserver.studyMate.domain.StudyMateProfile;
import com.example.danjamserver.studyMate.domain.UserSubject;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Redis에 저장할 StudyMateProfile의 필요한 정보만을 담을 DTO
public class StudyMateProfileDTO {

    private Long id;//StudyMateProfile의 id
    private Long userId; //StudyMateProfile의 userId
    private Set<StudyType> preferredStudyTypes; //선호하는 스터디 종류
    private StudyTime preferredStudyTime; //원하는 스터디 시간
    private Set<String> userSubjects; //수강중인 과목
    private AverageGrade averageGrade; //평균 학점

    /**
     * define : studyMateProfile Entity를 StudyMateProfileDTO로 변환하는 메서드.
     * when : Repository에서 반환된 StudyMateProfile 리스트룰 DTO로 변환할 때 사용.
     */
    public static List<StudyMateProfileDTO> of(List<StudyMateProfile> studyMateProfiles) {
        return studyMateProfiles.stream()
                .map(studyMateProfile -> StudyMateProfileDTO.builder()
                        .id(studyMateProfile.getId())
                        .userId(studyMateProfile.getUser().getId())
                        .preferredStudyTypes(studyMateProfile.getPreferredStudyTypes().stream().map(PreferredStudyType::getStudyType).collect(Collectors.toSet()))
                        .preferredStudyTime(studyMateProfile.getPreferredStudyTime())
                        .userSubjects(studyMateProfile.getUserSubjects().stream().map(UserSubject::getSubject).collect(Collectors.toSet()))
                        .averageGrade(studyMateProfile.getAverageGrade())
                        .build())
                .collect(Collectors.toList());
    }
}
