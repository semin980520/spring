package com.beyond.basic.b2_board.post.dtos;


import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreateDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @Size(max = 3000, message = "3000자를 넘으면 안됩니다.")
    private String contents;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category;
    @NotBlank(message = "이메일은 필수입니다.")
    private String authorEmail;
    public  Post toEntity(Author author){
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .category(this.category)
                .author(author)
//                .delYn("N")
                .build();
    }


}
