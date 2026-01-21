package com.beyond.basic.b2_board.author.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorUpdatePwDto {

    private String email;
    private String password;
}
