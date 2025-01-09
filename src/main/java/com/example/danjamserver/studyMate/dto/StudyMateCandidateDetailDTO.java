package com.example.danjamserver.studyMate.dto;

import com.example.danjamserver.mate.dto.BaseCandidateDetatilDTO;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import com.example.danjamserver.studyMate.enums.StudyType;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class StudyMateCandidateDetailDTO extends BaseCandidateDetatilDTO {

    //    //유저 정보
//    private String nickname;
//
//    //MyProfile정보
//    private String mbti;
//    private String major;
//    private String greeting;
//    private Integer entryYear;
//    private String birth;
//    private String profileImgUrl;
    private Integer gender; // 성별 0: 여자, 1: 남자

    //StudyMateProfile정보
    private StudyTime preferredStudyTime; // 희망 스터디 소요 시간
    private AverageGrade averageGrade; // 평균 학점
    private Set<StudyType> preferredStudyTypes; // 희망 스터디 과목
    private Set<String> subjects; // 수강중인 과목
}
