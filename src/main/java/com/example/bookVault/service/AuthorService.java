package com.example.bookVault.service;

import com.example.bookVault.entity.Author;
import com.example.bookVault.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author save(Author author) {
        return authorRepository.save(author);
    }
}
