package com.beyond.basic.b2_board.author.dtos;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorLoginDto {

    private String email;
    private String password;

    public Author toEntity(String encodedPassword){
        return Author.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
