package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {

    private List<Author> authorList = new ArrayList<>();
    private static Long staticId = 1L;

    public void save(Author author){
//        author.setId(staticId++);
        this.authorList.add(author);
    }
    public List<Author> findAll(){
        return this.authorList;
    }
    public Optional<Author> findById(Long id){
        Author author = null;
        for (Author a : this.authorList){
            if (a.getId().equals(id)){
                author = a;
            }
            }

        return Optional.ofNullable(author);
    }
}
