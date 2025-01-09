package com.example.danjamserver.mate.domain;

import com.example.danjamserver.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mate")
public class Mate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MateType mateType;

    @OneToMany(mappedBy = "mate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MateUser> mateUsers;
}

