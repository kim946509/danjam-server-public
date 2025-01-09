package com.example.danjamserver.common.domain;

import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "school")
public class School extends BaseTimeEntity {
    // 학교 교윳값
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학교 정보
    @Column(nullable = false)
    private String name;

    // 학교 한글 정보
    @Column(nullable = false)
    private String korName;
}
