package com.example.bookVault.repository;

import com.example.bookVault.entity.Book;
import com.example.bookVault.projection.BookSummary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
    List<Book> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT b FROM Book b JOIN b.author a WHERE a.name = :authorName")
    List<Book> findByAuthorName(@Param("authorName") String authorName);

    @Query(value="select * from books where page_count > :minPage", nativeQuery = true)
    List<Book> findByMinPageNative(@Param("minPage") int minPage);

    List<Book> findByTitleContaining(String keyword);

    List<Book> findByPageCountGreaterThan(int pageCount);

    List<Book> findByAuthorNameOrderByTitleAsc(String authorName);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findTop5ByOrderByPageCountDesc();

    Page<Book> findByTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT b.title as title, b.pageCount as pageCount, a.name as authorName FROM Book b LEFT JOIN b.author a")
    Page<BookSummary> findAllSummaries(Pageable pageable);
}
