package com.example.danjamserver.mate.domain;

import com.example.danjamserver.common.domain.School;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "schoolMajors")
public class SchoolMajors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String major;

    @Column(nullable = true)
    private String college;

    //schoolId로 학교 구분
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolId", nullable = false)
    private School school;
}
