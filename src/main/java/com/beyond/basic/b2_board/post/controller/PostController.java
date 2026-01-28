package com.beyond.basic.b2_board.post.controller;


import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.dtos.PostSearchDto;
import com.beyond.basic.b2_board.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post/create")
    public void postCreate(@RequestBody PostCreateDto dto){

        postService.save(dto);

    }
    @GetMapping("/posts")
//    페이징처리를 위한 데이터 요청 형식 : http://localhost:8080/posts?page=3&size=3&sort=title,asc
//    검색 + 페이징처리를 위한 데이터 요청 형식 : http://localhost:8080/posts?page=3&size=3&sort=title,asc&title=hello&category=경제
    public Page<PostListDto> findAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @ModelAttribute PostSearchDto searchDto){
        Page<PostListDto> dto = postService.findAll(pageable, searchDto);
//        log.info("dto : {}", searchDto);
        return dto;
    }
    @GetMapping("/post/{id}")
    public PostDetailDto findById(@PathVariable Long id){
        PostDetailDto dto = postService.findById(id);
        return dto;
    }
    @DeleteMapping("/post/{id}")
    public String postDelete(@PathVariable Long id){
        postService.postDelete(id);
        return "ok";
    }
    @PatchMapping("/post/{id}")
    public void postBack(@PathVariable Long id){
        postService.postBack(id);
    }
}
