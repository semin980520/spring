package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//ResponseEntity : http응답객체의 body뿐 아니라,
//상태코드 및 헤더요소로를 바꿔야 하는 경우에 사용
@RestController
@RequestMapping("/response_entity")
public class ResponseEntityController {

//@ResponseStatus 어노테이션 사용 : 상황에 따른 분기처리의 어려움
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation")
    public String annotation(){

        return "ok";
    }
//ResponseEntity 방식
    @GetMapping("/method1")
    public ResponseEntity<String> method1(){

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
    @GetMapping("/method2")
    public ResponseEntity<?> method2(){

        return new ResponseEntity<>("OK", HttpStatus.NOT_FOUND);
    }
//    가장 추천하는 방식
//    ResponseEntitiy, ?(any), 빌더패턴(.builder.build 생략)을 사용하여 상태코드, header, body를 쉽게 생성
    @GetMapping("/method3")
    public ResponseEntity<?> method3(){
        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(1L)
                .name("h1")
                .email("h1@naver.com")
                .password("1234")
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .header("Content-Type", "application/json") // header는 자주 사용 x
                .body(dto);
    }
    @GetMapping("/method4")
    public ResponseEntity<?> method4(){
        AuthorDetailDto dto = AuthorDetailDto.builder()
                .id(1L)
                .name("h1")
                .email("h1@naver.com")
                .password("1234")
                .build();
        return ResponseEntity.ok(dto);
    }
}