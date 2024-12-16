package com.mohamed.tamer.graphql.demo.graphql_demo.repository;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor_Id(Long authorId);
}
