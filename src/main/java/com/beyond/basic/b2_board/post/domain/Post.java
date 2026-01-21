package com.beyond.basic.b2_board.post.domain;


import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GeneratedColumn;

@Getter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    private String category;
    @Column(nullable = false)
    private String authorEmail;
    private String yes;
    private String no;

}
