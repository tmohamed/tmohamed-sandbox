package com.mohamed.tamer.graphql.demo.graphql_demo.repository;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
