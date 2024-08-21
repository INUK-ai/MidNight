package com.mid.night.member.domain;

import com.mid.night.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_tb")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nickName;
    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'USER'")
    private Authority authority;

    @Column
    private int plantNums;

    @Column
    private int sunny;
    @Column
    private int cloudy;
    @Column
    private int windy;
    @Column
    private int rainy;
    @Column
    private int snowy;

    @Builder
    public Member(String nickName, String password, Authority authority) {
        this.nickName = nickName;
        this.password = password;
        this.authority = authority;
        this.plantNums = 0;
        this.sunny = 2;
        this.cloudy = 2;
        this.windy = 2;
        this.rainy = 2;
        this.snowy = 2;
    }

    public void updateSunny(int count) {
        this.sunny += count;
    }

    public void updateCloudy(int count) {
        this.cloudy += count;
    }

    public void updateWindy(int count) {
        this.windy += count;
    }

    public void updateRainy(int count) {
        this.rainy += count;
    }

    public void updateSnowy(int count) {
        this.snowy = count;
    }

    public void initSunny(int count) {
        this.sunny = count;
    }

    public void initCloudy(int count) {
        this.cloudy = count;
    }

    public void initWindy(int count) {
        this.windy = count;
    }

    public void initRainy(int count) {
        this.rainy = count;
    }

    public void initSnowy(int count) {
        this.snowy = count;
    }

    public void updatePlantNums() {
        this.plantNums++;
    }

    public void initPlantNums(int count) {
        this.plantNums = count;
    }
}
