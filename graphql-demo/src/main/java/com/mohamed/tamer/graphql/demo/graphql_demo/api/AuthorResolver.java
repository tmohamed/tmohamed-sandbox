package com.mohamed.tamer.graphql.demo.graphql_demo.api;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Author;
import com.mohamed.tamer.graphql.demo.graphql_demo.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthorResolver {
    private final AuthorService authorService;

    @MutationMapping
    public Author createAuthor(@Argument String name, @Argument String bio) {
        return authorService.createAuthor(name, bio);
    }
}
