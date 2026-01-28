package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dtos.*;
import com.beyond.basic.b2_board.author.repository.AuthorJpaRepository;
import com.beyond.basic.b2_board.author.repository.AuthorMybatisRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
//Component 어노테이션을 통해 싱글톤(단 하나의) 객체가 생성되고, 스프링에 의해 스프링 컨텍스트에서 관리
@Service
//반드시 초기화 되어야하는 필드(final 변수 등)를 대상으로 생성자를 자동생성
//내가 필요한 변수들만 생성. AllArgs 필요 없음
//@RequiredArgsConstructor
//스프링에서 jpa를 활용할때 트렌잭션처리(commit, 롤백) 지원.
//commit의 기준점 : 메서드 정상 종료 시점.
//rollback의 기준점 : 예외 방생했을 때
@Transactional
public class AuthorService {
//    private final AuthorRepository authorRepository;
//    의존성주입(DI) 방법 1 . 필드(변수)주입 : AutoWired 어노테이션 사용 (간편방식)
//    @Autowired
//    private  AuthorRepository authorRepository;
//    의존성주입(DI)방법 2 . 생성자주입방식 (가장 많이 사용되는 방식)
//    장점1. final을 통해 상수로 사용가능 (안정성 향상)
//    장점2. 다형성 구현 가능 (interface사용가능);
//    장점3. 순환참조방지(컴파일 타임에 에러check) (3가지방법 모두 가능)
   private final AuthorRepository authorRepository;
   private final PostRepository postRepository;
   private final PasswordEncoder passwordEncoder;
//   생성자가 하나밖에 없을 때에는 Autowired생략 가능
   @Autowired
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder){
        this.authorRepository = authorRepository;
       this.postRepository = postRepository;
       this.passwordEncoder = passwordEncoder;
   }
//    의존성주입(DI)방법 3. RequiredArgsConstructor어노테이션 사용
//    반드시 초기화되어야 하는 필드를 선언하고, 위 어노테이션 선언시 생성자주입방식으로 의존성이 주입됨
//    다형성 설계 불가능
    public void save(AuthorCreateDto dto){
//        방법1.  객체 직접 조립
//        1-1). 생성자만을 활용한 객체 조립
//        Author author = new Author(null, dto.getName(), dto.getEmail(), dto.getPassword());
//        1-2). builder패턴을 활용해 객체 조립
//        장점1. 매개변수 개수의 유연성
//        장점2. 매개변서의 순서의 유연성
//        Author author = Author.builder()
//                .email(dto.getEmail())
//                .name(dto.getName())
//                .password(dto.getPassword())
//                .build();
//        방법2. toEntity, fromEntity 패턴을 통한 객체조립
//        객체조립이라는 반복작업을 별도의 코드로 때어내 공통화

        if (authorRepository.findAllByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("중복되는 이메일이 있습니다.");
        }

        Author author = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        authorRepository.save(author);
//        cascade persist를 활용한 예시
        Author authorDb = authorRepository.save(author);
      author.getPostList().add(Post.builder()
                        .author(author)
                        .title("안녕하세요")
                        .build());
//        cascade 옵션이 아닌 예시
//        postRepository.save(Post.builder()
//                        .title("안녕하세요")
//                        .author(authorDb)
//                        .build());
//        예외 발생시 transactional 어노테이션에 의해 rollback 처리
//        authorRepository.findById(10L).orElseThrow(()-> new NoSuchElementException("허허허"));
    }
//    트랜잭션 처리가 필요없는 조회만 있는 메서드의 경우 성능향상을 위해 readOnly 처리
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id){
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = optAuthor.orElseThrow(()-> new NoSuchElementException("entity is not found"));

//        List<Post> postList = postRepository.findAllByAuthorIdAndDelYn(author.getId(),"N");
//        dto 조립
//        AuthorDetailDto dto = new AuthorDetailDto(author.getId(),author.getName(), author.getEmail());
//        fromEntity는 아직 dto객체가 만들어지지 않은 상태이므로 static메서드로 설계
//        AuthorDetailDto dto1 = AuthorDetailDto.builder().id(author.getId()).email(author.getEmail()).name(author.getName()).build();
//        AuthorDetailDto dto2 = AuthorDetailDto.fromEntity(author, 0);
        AuthorDetailDto dto2 = AuthorDetailDto.fromEntity(author);
        return dto2;
    }
    public AuthorDetailDto myinfo(){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Author author = authorRepository.findAllByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return dto;
    }
    public List<AuthorListDto> findAll(){
//        List<Author> authors = authorRepository.findAll();
//        List<AuthorListDto> list = new ArrayList<>();
//        for (int i = 0; i < authors.size(); i++) {
//            Author author = authors.get(i);
//            AuthorListDto dto = new AuthorListDto(author.getId(),author.getName(),author.getEmail());
//        list.add(dto);
//        }
//        List<Author> authors = authorRepository.findAll();
//        List<AuthorListDto> list = new ArrayList<>();
//        for(Author author : authors){
//            AuthorListDto dto = AuthorListDto.fromEntity(author);
//            list.add(dto);
//        }
        List<AuthorListDto> list =
        authorRepository.findAll().stream().map(a->AuthorListDto.fromEntity(a)).collect(Collectors.toList());
        return list;
    }
    public void delete(Long id){
        Author author = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("없음"));

        authorRepository.delete(author);
   }
   public void update(AuthorUpdatePwDto dto){
       String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
       log.info(email);
        Author author = authorRepository.findAllByEmail(email).orElseThrow(()->new EntityNotFoundException("없음"));
            author.updatePassword(passwordEncoder.encode(dto.getPassword()));

//            insert, update 모두 save메서드 사용 -> 변경감지로 대체
//        authorRepository.save(author);

//          영속성컨텍스트 : 애플리케이션과 DB사이에서 객체를 보관하는 가상의 DB 역할
//          1) 쓰기지연 : insert, update 등의 작업사항을 즉시 실행하지 않고, 커밋시점에 모아서 실행(성능향상)
//          2) 변경감지(dirty checking) : 영속상태(managed)의 엔티티는 트랜잭션 커밋시점에 변경감지를 통해 별도의 save없이 DB에 반영
   }
   public Author login(AuthorLoginDto dto){
       Optional<Author> opt_author = authorRepository.findAllByEmail(dto.getEmail());
       boolean check = true;
       if (!opt_author.isPresent()){
           check = false;
       }else {
           if (!passwordEncoder.matches(dto.getPassword(), opt_author.get().getPassword())){
               check = false;
           }
       }
       if (!check){
           throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
       }

        return opt_author.get();
   }
}