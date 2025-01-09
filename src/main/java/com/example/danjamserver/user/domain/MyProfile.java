package com.example.danjamserver.user.domain;

import com.example.danjamserver.util.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "myProfile", indexes = {
        @Index(name = "idx_mbti", columnList = "mbti"),
        @Index(name = "idx_entryYear", columnList = "entryYear"),
        @Index(name = "idx_major", columnList = "major")
})
public class MyProfile extends BaseTimeEntity {
    // 프로필 고윳값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 생성시 동시에 저장
    @Column(nullable = false)
    private String birth;
    @Column(nullable = false)
    private Integer entryYear;
    @Column(nullable = false)
    private String major;
    @Column(columnDefinition = "TEXT")
    private String profileImgUrl;

    //프로필 정보
    @Column(nullable = true)
    String mbti; // mbti
    @Column(nullable = true)
    String greeting; // 인삿말&자기소개&소갯말등

    // User 정보
    @OneToOne(mappedBy = "myProfile", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}
