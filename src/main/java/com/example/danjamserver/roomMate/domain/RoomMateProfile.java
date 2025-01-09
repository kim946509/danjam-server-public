package com.example.danjamserver.roomMate.domain;

import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.roomMate.enums.ActivityTime;
import com.example.danjamserver.roomMate.enums.CleanPeriod;
import com.example.danjamserver.roomMate.enums.Level;
import com.example.danjamserver.roomMate.enums.ShowerTime;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roomMateProfile", indexes = {
        @Index(name = "idx_fitlering", columnList = "isSmoking, hotLevel, coldLevel, activityTime, cleanPeriod, showerTime")
})
public class RoomMateProfile extends MateProfile {

    @Column(nullable = false)
    Boolean isSmoking; //흡연자, 비흡연자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Level hotLevel; // 많이타요, 적게타요, 상관없어요

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Level coldLevel; // 많이타요, 적게타요, 상관없어요

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ActivityTime activityTime; // 아침형, 새벽형, 상관없어요

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    CleanPeriod cleanPeriod; // 매일해요, 주마다해요, 달마다해요, 상관없어요

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ShowerTime showerTime; // _10_20분, _20_30분, _30_40분, _40분이상

    @OneToMany(mappedBy = "roomMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HopeDormitory> hopeDormitories = new ArrayList<>();

    @OneToMany(mappedBy = "roomMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HopeRoomPerson> hopeRoomPersons = new ArrayList<>();

    @OneToMany(mappedBy = "roomMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OwnSleepHabit> ownSleepHabits = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
