package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.AuthorListDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dtos.PostCreateDto;
import com.beyond.basic.b2_board.post.dtos.PostDetailDto;
import com.beyond.basic.b2_board.post.dtos.PostListDto;
import com.beyond.basic.b2_board.post.dtos.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
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
//        Author author = authorRepository.findAllByEmail(dto1.getAuthorEmail())
//                .orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println(email);
        Author author = authorRepository.findAllByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));
        Post createPost = dto1.toEntity(author);
        postRepository.save(createPost);


    }
    @Transactional(readOnly = true)
    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto searchDto){
//        List<Post> postList = postRepository.findAllByDelYn("N");
//        List<PostListDto> dtoList = new ArrayList<>();
//        for (Post p : postList) {
////            Author author = authorRepository.findById(p.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("아이디가 없습니다"));
//            PostListDto dto = PostListDto.fromEntity(p);
//            dtoList.add(dto);
//        }

//        List<PostListDto> dto = postRepository.findAllByDelYn("N")
//        List<PostListDto> dto = postRepository.findAllFetchInnerJoin()
//                .stream()
//                .map(a->PostListDto.fromEntity(a))
//                .collect(Collectors.toList());
//        Pase 객체 안에 Entity -> Dto로 쉽게 변환 할 수 있는 편의 제공
//        Page<Post> postList = postRepository.findAll(pageable);

//        검색을 위한 Specification 객체 조립
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                root : 엔티티의 컬럼명을 접근하기 위한 객체, criteriaBuilder는 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N"));
                if (searchDto.getTitle() != null){
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + searchDto.getTitle() + "%"));
                }
                if (searchDto.getCategory() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                }
                if (searchDto.getContents() != null){
                    predicateList.add(criteriaBuilder.like(root.get("contents"), "%" + searchDto.getContents() + "%"));
                }
                Predicate[] predicatesArr = new Predicate[predicateList.size()];
                for (int i=0; i<predicatesArr.length; i++){
                    predicatesArr[i] = predicateList.get(i);
                }
//                Predicate에는 검색조건들이 닮길 것 이고, 이 Predicate리스트를 한줄의 predicate조립.
                Predicate predicate = criteriaBuilder.and(predicatesArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification, pageable);
        Page<PostListDto> dto = postList.map(p->PostListDto.fromEntity(p));
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
