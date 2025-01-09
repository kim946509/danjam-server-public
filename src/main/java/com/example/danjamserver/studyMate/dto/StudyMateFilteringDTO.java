package com.example.danjamserver.studyMate.dto;

import com.example.danjamserver.mate.dto.BaseFilteringDTO;
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
public class StudyMateFilteringDTO extends BaseFilteringDTO {
    //    String mbti;
//    String minBirthYear;
//    String maxBirthYear;
//    Integer minEntryYear;
//    Integer maxEntryYear;
//    List<String> colleges;
    Set<Integer> gender; // 0: 여자, 1: 남자

    //StudyMateProfile정보
    private Set<StudyType> preferredStudyTypes; // 희망하는 스터디
    private Set<StudyTime> preferredStudyTimes; // 희망하는 스터디 소요 시간
    private Set<AverageGrade> preferredAverageGrades; // 평균 학점

}
