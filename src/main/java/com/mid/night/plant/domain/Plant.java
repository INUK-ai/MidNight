package com.mid.night.plant.domain;

import com.mid.night.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plant_tb")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 255, nullable = false)
    private String plantName;
    @Column
    private int GrowthGauge;

    @Builder
    public Plant(Member member) {
        this.member = member;
        this.plantName = "토마토";
        this.GrowthGauge = 0;
    }
}
