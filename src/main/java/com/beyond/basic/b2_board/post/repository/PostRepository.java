package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByDelYn(String Y);
//    List<Post> findAllByAuthorIdAndDelYn(Long authorId, String DelYn);

//    jpql을 활용한 일반 inner join : N+1문제 해결 X
//    jpql과 raw쿼리의 차이
//    1. jpql을 사용한 inner join 시, 별도의 on조건 필요X
//    2. jpql은 컴파일타임의 에러를 check
//    순수raw : select p.* from Post p inner join author a on a.id = p.author.id
    @Query("select p from Post p inner join p.author")
    List<Post> findAllInnerJoin(); // 위 지정한 쿼리가 네이밍한 객체에 들어옴
//    jpql을 활용한 inner join(fetch) : N+1문제 해결 O
//    순수raw : select * from post p inner join author a on a.id = p.author.id
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchInnerJoin();

}