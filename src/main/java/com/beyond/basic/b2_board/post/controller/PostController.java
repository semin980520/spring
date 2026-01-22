package com.beyond.basic.b2_board.post.controller;


import com.beyond.basic.b2_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<PostListDto> findAll(){
        List<PostListDto> dto1 = postService.findAll();
        return dto1;
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
