package com.beyond.basic.b2_board.post.domain;


import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    private String category;
//    @Column(nullable = false)
//    private Long authorId;
//    ManyToOne을 통해 fk설정(author_id컬럼)
//    ManyToOne을 통해 author_id컬럼으로 author객체 조회 자동 생성
//    fetch lazy(지연로딩) : author객체를 사용하지 않는한, author객체 생성x (서버 부하 감소)
    @ManyToOne(fetch = FetchType.LAZY) // n:1  N쪽에는 필수적으로 걸고 1쪽에는 안걸어도 가능 fk는 n에 설정
//    ManyToOne 어노테이션만 추가하더라도, 아래 옵션이 생략돼 있는것. fk를 설정하지 않고자 할 대, NO_CONSTRAINT 설정
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false) // db 컬러명 저장 , fk지정
    private Author author; //객체로 저장
    @Builder.Default
    private String delYn = "N";

    @Builder.Default
    private String appointment = "N";
    @Builder.Default
    private LocalDateTime appointmentTime = LocalDateTime.now();
    public void delete() {
        this.delYn = "Y";
    }
    public void back(){
        this.delYn = "N";
    }
    public void updateAppointment(String appointment){
        this.appointment = appointment;
    }

}
