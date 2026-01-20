package com.beyond.basic.b2_board.author.domain;


import lombok.*;

// entity에서는 setter 안쓰는게 기본 방식
@Getter
@ToString
//빌더패턴은 AllArgs생성자 기반으로 동작
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;
}
