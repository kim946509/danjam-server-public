package com.example.danjamserver.user.domain;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.mate.domain.MateLike;
import com.example.danjamserver.mate.domain.MateProfile;
import com.example.danjamserver.mate.domain.MateUser;
import com.example.danjamserver.notice.domain.FcmTopic;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET deletedAt = current_timestamp Where id = ?")
@OnDelete(action = OnDeleteAction.CASCADE)
@Where(clause = "deletedAt is null")
@Table(name = "user", indexes = {
        @Index(name = "idx_school_gender", columnList = "schoolId, gender"),
        @Index(name = "idx_username", columnList = "username") // username 인덱스 추가

})
public class User extends BaseTimeEntity {

    // 유저 고윳값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 승인 관련 정보
    @Column(nullable = false, unique = true)
    private String username;//login Id
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(columnDefinition = "TEXT")
    private String authImgUrl;
    @Column(columnDefinition = "TEXT")
    private String residentImgUrl;
    @Enumerated(EnumType.STRING)
    private Role role;

    // 유저 생성 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolId", nullable = false)
    private School school;
    @Column(nullable = false)
    private Integer gender;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    // 유저 프로필
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "myProfileId")
    private MyProfile myProfile;

    // 메이트 정보
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MateUser> mateUsers;

    // 메이트 프로필
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MateProfile> mateProfiles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MateLike> mateLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Report> reports;

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Block> blocksInitiated;

    @OneToMany(mappedBy = "blocked", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Block> blocksReceived;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "userTopic",  //mapping table
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "topicId")
    )
    private Set<FcmTopic> topics = new HashSet<>();
}
