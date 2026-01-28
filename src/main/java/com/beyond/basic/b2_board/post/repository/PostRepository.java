package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
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

//    Page객체 안에는 content(List<Post>), totalPages, totalElement등의 정보 포함
//    Page<Post> findAllDelYn(Pageable pageable, String delYn);

//    검색+ 페이징처리까지 할 경우, 아래와 같이 매개변수 선언(Specification, Pageable 순서 - SimpleJpaRepository에서 정의)
//    @EntityGraph(attributePaths = "author") // fetch join 사용 어노테이션
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);
//    예약글쓰기를 위한 findAllByAppointment 별도 추가
    List<Post> findAllByAppointment(String appointment);
}