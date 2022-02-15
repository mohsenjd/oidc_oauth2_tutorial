package com.mohsen.bookservice;

import com.mohsen.bookservice.model.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class BookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( request -> request.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .build();
    }
}
@RestController
@RequestMapping("books")
class BookController {
    private static final Map<String, Book> books = new HashMap<>();

    static {
        Book book1 = new Book("123", "Kite Runner");
        Book book2 = new Book("321", "The Hills Have Eyes");
        books.put(book1.getIsbn(), book1);
        books.put(book2.getIsbn(), book2);
    }

    @GetMapping()
    Collection<Book> getBooks(){
        return books.values();
    }

    @GetMapping("{isbn}")
    Optional<Book> getBookByIsbn(@PathVariable String isbn){
        return Optional.of(books.get(isbn));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Book createBook(@RequestBody Book book){
        books.put(book.getIsbn(), book);
        return book;
    }
}
