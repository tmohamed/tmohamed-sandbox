package com.mohamed.tamer.graphql.demo.graphql_demo.repository;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
