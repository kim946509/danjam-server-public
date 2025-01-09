package com.example.danjamserver.home.domain;

import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mySchedule")
/**
 * 나의 스케줄
 * @version 1.0.0
 * @param
 *     id 스케줄 고유값,
 *     title 스케줄 제목
 *     memo 스케줄 메모. null 가능
 *     startDate 스케줄 시작일자(년/월/일/시작시간). null 가능
 *     endDate 스케줄 종료일자(년/월/일/종료시간). null 가능
 *     alarm 알람시간(년/월/일/시간). null 가능
 *     user 사용자
 */
public class MySchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String memo;

    @Column(nullable = true)
    private LocalDateTime startDate;

    @Column(nullable = true)
    private LocalDateTime endDate;

    @Column(nullable = true)
    private LocalDateTime alarm;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
