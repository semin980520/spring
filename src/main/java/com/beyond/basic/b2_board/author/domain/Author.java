package com.beyond.basic.b2_board.author.domain;


import jakarta.persistence.*;
import lombok.*;

// entity에서는 setter 안쓰는게 기본 방식
@Getter
@ToString
//빌더패턴은 AllArgs생성자 기반으로 동작
@Builder
@AllArgsConstructor
@NoArgsConstructor
//JPA에게 Entity관리를 위임하기 위한 어노테이션
@Entity // 엔티티를 기준으로 db 테이블 생성
public class Author {
    @Id //pk설정
//    identity : auto_increment설정. auto : id생성전락을 jpa에게 자동설정하도록 위임.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long -> bigint, String -> varchar
    private Long id;
//    변수명이 컬럼명으로 그대로 생성. nickName. camel case는 언더스코어로 변경 예) nickName -> nick_name
    private String name;
//    길이를(varchar50, 디폴트=varchar255),제약조건(unique,not null) 설정
    @Column(length = 50,unique = true,nullable = false)
    private String email;
//    @Column(name = "pw") : 컬럼명의 변경이 가능하나, 일반적으로 일치시킴.
//    @Setter // 컬럼 위에 Setter 붙일 수 있음.
    private String password;

    public void updatePassword(String password){
        this.password = password;
    }
}
