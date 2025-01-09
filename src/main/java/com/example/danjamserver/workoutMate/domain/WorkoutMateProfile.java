package com.example.danjamserver.workoutMate.domain;

import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.workoutMate.enums.WorkoutIntensity;
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
@Table(name = "workoutMateProfile")
public class WorkoutMateProfile extends MateProfile {

    //선호하는 운동 시간대
    @Column(nullable = false)
    @OneToMany(mappedBy = "workoutMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredWorkoutTimeZone> preferredWorkoutTimeZones;

    //선호하는 운동 강도
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutIntensity preferredWorkoutIntensity;

    //선호하는 운동
    @OneToMany(mappedBy = "workoutMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredWorkoutType> preferredWorkoutTypes;

    //선호하는 운동 소요 시간
    @OneToMany(mappedBy = "workoutMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredWorkoutTime> preferredWorkoutTimes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
