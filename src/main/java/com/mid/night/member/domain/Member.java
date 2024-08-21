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
        this.sunny = 5;
        this.cloudy = 4;
        this.windy = 3;
        this.rainy = 2;
        this.snowy = 1;
    }
}
