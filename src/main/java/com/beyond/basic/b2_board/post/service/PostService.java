package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto1){
         authorRepository.findByEmail(dto1.getAuthorEmail()).orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));

        Post createPost = dto1.toEntity();
        postRepository.save(createPost);

    }
    public List<PostListDto> findAll(){
        List<PostListDto> post = postRepository.findAll().stream().map(a->PostListDto.fromEntity(a)).collect(Collectors.toList());
       return post;

    }
    public PostDetailDto findById(Long id){
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = optAuthor.orElseThrow(()-> new NoSuchElementException("entity is not found"));
        Optional<Post> optPost = postRepository.findById(id);
        Post post = optPost.orElseThrow(()->new EntityNotFoundException("해당 아이디의 게시글이 없습니다"));
        PostDetailDto dto = PostDetailDto.fromEntity(post);
        return dto;
    }
}
