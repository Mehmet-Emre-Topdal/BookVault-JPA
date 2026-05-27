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

    // ─── FETCH DEMO METODLARI ─────────────────────────────────────────────────

    // 1. JOIN FETCH (JPQL)
    // Tek bir SELECT ile book + author birlikte çeker.
    // Üretilen SQL: SELECT b.*, a.* FROM books b INNER JOIN authors a ON b.author_id = a.id
    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    List<Book> findAllJoinFetchAuthor();

    // 2. JOIN FETCH - birden fazla koleksiyon
    // DISTINCT zorunlu: OneToMany join'i sonuçta kitap başına birden fazla satır üretebilir.
    // Üretilen SQL: books + authors + reviews tek sorguda LEFT JOIN ile
    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.author LEFT JOIN FETCH b.reviews")
    List<Book> findAllJoinFetchAuthorAndReviews();

    // 3. Inline @EntityGraph - attributePaths ile
    // Hibernate bu metod çağrıldığında author'u JOIN ile çeker.
    // Üretilen SQL: SELECT b.*, a.* FROM books b LEFT OUTER JOIN authors a ON ...
    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT b FROM Book b")
    List<Book> findAllWithAuthor();

    // 4. Inline @EntityGraph - birden fazla ilişki
    // author + reviews + tags tek sorguda (veya birkaç sorguda) çekilir.
    // Üretilen SQL: books LEFT JOIN authors LEFT JOIN reviews LEFT JOIN book_tags LEFT JOIN tags
    @EntityGraph(attributePaths = {"author", "reviews", "tags"})
    @Query("SELECT DISTINCT b FROM Book b")
    List<Book> findAllWithAll();

    // 5. Named @EntityGraph - entity üzerinde tanımlı isimli grafik
    // Book.withAuthorAndReviews: author + reviews birlikte
    // Üretilen SQL: books LEFT JOIN authors LEFT JOIN reviews (tek sorgu)
    @EntityGraph("Book.withAuthorAndReviews")
    Optional<Book> findWithReviewsById(Long id);

    // 6. Named @EntityGraph - tüm ilişkiler
    @EntityGraph("Book.withAll")
    @Query("SELECT DISTINCT b FROM Book b WHERE b.id = :id")
    Optional<Book> findWithAllById(@Param("id") Long id);
}
