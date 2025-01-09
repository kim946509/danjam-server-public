package com.example.danjamserver.foodMate.domain;


import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.mate.domain.MateProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "foodMateProfile")
public class FoodMateProfile extends MateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "foodMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MealMateTime> mateTime = new HashSet<>();

    @OneToMany(mappedBy = "foodMateProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HopeMenu> hopeMenu = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealTime mealTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiningManner diningManner;

    @Column(columnDefinition = "JSON")
    private String allergies;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Map<String, Object>을 콤마로 구분된 문자열로 변환하여 저장하는 헬퍼 메서드
    public void setAllergiesMap(Map<String, Object> allergiesMap) {
        try {
            this.allergies = objectMapper.writeValueAsString(allergiesMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert allergiesMap to JSON string", e);
        }
    }
}
