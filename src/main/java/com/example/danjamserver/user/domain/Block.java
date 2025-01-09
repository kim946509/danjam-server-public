package com.example.danjamserver.user.domain;

import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "block", indexes = {
        @Index(name = "idx_blocker_blocked", columnList = "blocker, blocked")
})
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차단한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker", nullable = false)
    private User blocker;

    // 차단된 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked", nullable = false)
    private User blocked;

    // 차단 여부
    @Column(nullable = false)
    private Boolean isBlocked;

    public void changeBlockedState(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}
