package com.mohamed.tamer.graphql.demo.graphql_demo.service;

import com.mohamed.tamer.graphql.demo.graphql_demo.model.Author;
import com.mohamed.tamer.graphql.demo.graphql_demo.model.Book;
import com.mohamed.tamer.graphql.demo.graphql_demo.repository.AuthorRepository;
import com.mohamed.tamer.graphql.demo.graphql_demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> getAllBooksPaginated(Integer offset, Integer limit){
        Pageable pageable = PageRequest.of(offset != null ? offset : 0, limit != null ? limit : 10);
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }



    public Book createBook(String title, Integer publishedYear, Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
        Book book = new Book();
        book.setTitle(title);
        book.setPublishedYear(publishedYear);
        book.setAuthor(author);
        return bookRepository.save(book);
    }
}
