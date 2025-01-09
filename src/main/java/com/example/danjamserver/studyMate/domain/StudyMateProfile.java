package com.example.danjamserver.studyMate.domain;

import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.studyMate.enums.AverageGrade;
import com.example.danjamserver.studyMate.enums.StudyTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "studyMateProfile")
public class StudyMateProfile extends MateProfile {

    // 선호하는 스터디 종류
    @Column(nullable = false)
    @OneToMany(mappedBy = "studyMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredStudyType> preferredStudyTypes;

    // 원하는 스터디 지간
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyTime preferredStudyTime;

    // 수강중인 과목
    @Column(nullable = true)
    @OneToMany(mappedBy = "studyMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserSubject> userSubjects;

    // 평균 학점
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AverageGrade averageGrade;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
