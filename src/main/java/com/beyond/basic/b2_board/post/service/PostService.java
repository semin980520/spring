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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto1){
        Author author = authorRepository.findAllByEmail(dto1.getAuthorEmail())
                .orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));
        System.out.println(dto1);
        System.out.println(author);
        Post createPost = dto1.toEntity(author);
        System.out.println(createPost);
        postRepository.save(createPost);


    }
    @Transactional(readOnly = true)
    public List<PostListDto> findAll(){
//        List<Post> postList = postRepository.findAllByDelYn("N");
//        List<PostListDto> dtoList = new ArrayList<>();
//        for (Post p : postList) {
////            Author author = authorRepository.findById(p.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("아이디가 없습니다"));
//            PostListDto dto = PostListDto.fromEntity(p);
//            dtoList.add(dto);
//        }

        List<PostListDto> dto = postRepository.findAllByDelYn("N")
                .stream()
                .map(a->PostListDto.fromEntity(a))
                .collect(Collectors.toList());
        System.out.println(dto);
        return dto;

    }
    @Transactional(readOnly = true)
    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 게시글이 없습니다"));
        Author author = authorRepository.findById(id).orElseThrow(()->new EntityNotFoundException("아이디가 없습니다"));
        if ("Y".equals(post.getDelYn())) {
            throw new EntityNotFoundException("삭제된 게시글입니다.");
        }

//        PostDetailDto dto = PostDetailDto.fromEntity(post,author);
        PostDetailDto dto = PostDetailDto.fromEntity(post);
        System.out.println(dto);
        return dto;

    }
    public void postDelete(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

        post.delete();

//        save x 변경 감지
    }
    public void postBack(Long id){
    Post post = postRepository.findById(id)
            .orElseThrow(()->new EntityNotFoundException("게시글이 없습니다."));
    if ("N".equals(post.getDelYn())){
        throw new EntityNotFoundException("삭제되지 않은 게시글입니다.");
    }
    post.back();
//    save x 변경 감지
    }
}
