package com.example.bookVault.controller;

import com.example.bookVault.entity.Author;
import com.example.bookVault.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public Author create(@RequestBody Author author) {
        return authorService.save(author);
    }
}
