package com.mohamed.tamer.graphql.demo.graphql_demo.api;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Book;
import com.mohamed.tamer.graphql.demo.graphql_demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookResolver {
    private final BookService bookService;

    @QueryMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @QueryMapping
    public List<Book> getAllBooksPaginated(@Argument Integer offset, @Argument Integer limit) {
        return bookService.getAllBooksPaginated(offset, limit).getContent();
    }

    @QueryMapping
    public Book getBookById(@Argument Long id) {
        return bookService.getBookById(id);
    }

    @MutationMapping
    public Book createBook(String title, Integer publishedYear, Long authorId) {
        return bookService.createBook(title, publishedYear, authorId);
    }

}
