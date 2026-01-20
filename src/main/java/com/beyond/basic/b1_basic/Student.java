package com.beyond.basic.b1_basic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;
    private String email;
    private List<Scores> Scores;

//    static 으로 하나의 변수처럼 Scores를 사용
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Scores {

    private String subject;
    private int point;

}

}

