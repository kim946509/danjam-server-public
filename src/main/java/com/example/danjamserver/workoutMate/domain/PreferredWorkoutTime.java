package com.example.danjamserver.workoutMate.domain;

import com.example.danjamserver.workoutMate.enums.WorkoutTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preferredWorkoutTime")
public class PreferredWorkoutTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkoutTime workoutTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workoutMateProfileId", nullable = false)
    private WorkoutMateProfile workoutMateProfile;

}
