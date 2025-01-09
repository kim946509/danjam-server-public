package com.example.danjamserver.admin.domain;


import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rejectionReason")
public class RejectionReason extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String reason;
}

