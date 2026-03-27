package com.example.bookVault.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookVault.entity.Book;
import com.example.bookVault.projection.BookSummary;
import com.example.bookVault.repository.BookRepository;
import com.example.bookVault.specification.BookSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found for id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
public List<Book> searchByTitle(String keyword) {
    return bookRepository.searchByTitle(keyword);
}

@Transactional(readOnly = true)
public List<Book> findByAuthorName(String authorName) {
    return bookRepository.findByAuthorName(authorName);
}

@Transactional(readOnly = true)
public Page<BookSummary> findAllSummaries(int page, int size) {
    return bookRepository.findAllSummaries(PageRequest.of(page, size));
}

@Transactional(readOnly = true)
public Page<Book> search(String title, String authorName, Integer minPage, int page, int size) {
    Specification<Book> spec = Specification
        .where(BookSpecification.titleContains(title))
        .and(BookSpecification.authorNameEquals(authorName))
        .and(BookSpecification.minPageCount(minPage));
    return bookRepository.findAll(spec, PageRequest.of(page, size));
}

}
