package com.example.danjamserver.walkMate.domain;

import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.walkMate.enums.WalkTime;
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
@Table(name = "walkMateProfile")
public class WalkMateProfile extends MateProfile {

    //선호하는 산책 시간대
    @Column(nullable = false)
    @OneToMany(mappedBy = "walkMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredWalkTimeZone> preferredWalkTimeZones;

    //선호하는 산책 소요 시간
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WalkTime preferredWalkTime;

    //선호하는 산책 강도
    @OneToMany(mappedBy = "walkMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PreferredWalkIntensity> preferredWalkIntensities;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
