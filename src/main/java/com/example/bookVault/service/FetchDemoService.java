package com.example.bookVault.service;

import com.example.bookVault.entity.Book;
import com.example.bookVault.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FetchDemoService {

    private final BookRepository bookRepository;

    // ─── SENARYO 1: N+1 Problemi ──────────────────────────────────────────────
    // findAll() → LAZY author → her book için ayrı SELECT author
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT * FROM books          (kitapları çek)
    //   Nx  SELECT * FROM authors WHERE id = ?   (her kitap için ayrı yazar sorgusu)
    //       (N = kitap sayısı kadar)
    //
    // @PostLoad içinde author.getName() çağrıldığından author her durumda yüklenir.
    // Bu, N+1 probleminin klasik örneğidir.
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioNPlusOne() {
        List<Book> books = bookRepository.findAll();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scenario", "N+1 Problem - findAll() ile LAZY author");
        result.put("aciklama", "1 SELECT books + N SELECT authors (N = " + books.size() + ")");
        result.put("books", books.stream().map(b -> b.getTitle() + " → " + b.getAuthor().getName()).toList());
        return result;
    }

    // ─── SENARYO 2: JOIN FETCH (JPQL) ─────────────────────────────────────────
    // JPQL'de JOIN FETCH yazarak tek sorguda book + author çekiyoruz.
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT b.*, a.*
    //       FROM books b
    //       INNER JOIN authors a ON b.author_id = a.id
    //
    // N+1 problemi tamamen ortadan kalkar.
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioJoinFetch() {
        List<Book> books = bookRepository.findAllJoinFetchAuthor();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scenario", "JOIN FETCH - JPQL ile tek sorgu");
        result.put("aciklama", "1 SQL: SELECT books + INNER JOIN authors");
        result.put("books", books.stream().map(b -> b.getTitle() + " → " + b.getAuthor().getName()).toList());
        return result;
    }

    // ─── SENARYO 3: JOIN FETCH - Birden Fazla Koleksiyon ─────────────────────
    // book + author + reviews tek sorguda.
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT DISTINCT b.*, a.*, r.*
    //       FROM books b
    //       INNER JOIN authors a ON b.author_id = a.id
    //       LEFT OUTER JOIN reviews r ON r.book_id = b.id
    //
    // DISTINCT: Her review için aynı book tekrarlanmaması için gerekli.
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioJoinFetchWithReviews() {
        List<Book> books = bookRepository.findAllJoinFetchAuthorAndReviews();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scenario", "JOIN FETCH - author + reviews tek sorguda");
        result.put("aciklama", "1 SQL: SELECT DISTINCT books + INNER JOIN authors + LEFT JOIN reviews");
        result.put("books", books.stream().map(b ->
            b.getTitle() + " → " + b.getAuthor().getName() + " (" + b.getReviews().size() + " review)"
        ).toList());
        return result;
    }

    // ─── SENARYO 4: @EntityGraph - Inline attributePaths ─────────────────────
    // Repository metoduna @EntityGraph(attributePaths = {"author"}) ekleyerek
    // Hibernate'e "bu sorguda author'u da getir" diyoruz.
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT b.*, a.*
    //       FROM books b
    //       LEFT OUTER JOIN authors a ON b.author_id = a.id
    //
    // JOIN FETCH'ten farkı: LEFT OUTER JOIN kullanır (author null olabilir).
    // JOIN FETCH INNER JOIN kullanır (author null olamaz).
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioEntityGraphInline() {
        List<Book> books = bookRepository.findAllWithAuthor();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scenario", "@EntityGraph(attributePaths = {\"author\"})");
        result.put("aciklama", "1 SQL: SELECT books + LEFT OUTER JOIN authors");
        result.put("fark", "JOIN FETCH INNER JOIN, EntityGraph LEFT OUTER JOIN kullanır");
        result.put("books", books.stream().map(b -> b.getTitle() + " → " + b.getAuthor().getName()).toList());
        return result;
    }

    // ─── SENARYO 5: @EntityGraph - Tüm İlişkiler ─────────────────────────────
    // author + reviews + tags hepsini tek sorguda çekiyoruz.
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT DISTINCT b.*, a.*, r.*, bt.*, t.*
    //       FROM books b
    //       LEFT OUTER JOIN authors a ON ...
    //       LEFT OUTER JOIN reviews r ON ...
    //       LEFT OUTER JOIN book_tags bt ON ...
    //       LEFT OUTER JOIN tags t ON ...
    //
    // Dikkat: Çok fazla JOIN büyük tablolarda performansı düşürebilir.
    // Sadece gerçekten ihtiyaç duyulan ilişkileri eager yükle!
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioEntityGraphAll() {
        List<Book> books = bookRepository.findAllWithAll();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scenario", "@EntityGraph(attributePaths = {\"author\", \"reviews\", \"tags\"})");
        result.put("aciklama", "1 SQL: books + LEFT JOIN authors + LEFT JOIN reviews + LEFT JOIN tags");
        result.put("books", books.stream().map(b ->
            b.getTitle()
            + " | yazar: " + b.getAuthor().getName()
            + " | " + b.getReviews().size() + " review"
            + " | tags: " + b.getTags().stream().map(t -> t.getName()).toList()
        ).toList());
        return result;
    }

    // ─── SENARYO 6: Named @EntityGraph - Tek Kitap ───────────────────────────
    // @NamedEntityGraph("Book.withAuthorAndReviews") - entity üzerinde tanımlı.
    // findById gibi tek kayıt metodlarında da kullanılabilir.
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT b.*, a.*, r.*
    //       FROM books b
    //       LEFT OUTER JOIN authors a ON b.author_id = a.id
    //       LEFT OUTER JOIN reviews r ON r.book_id = b.id
    //       WHERE b.id = ?
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioNamedEntityGraph(Long bookId) {
        return bookRepository.findWithReviewsById(bookId).map(b -> {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("scenario", "@NamedEntityGraph(\"Book.withAuthorAndReviews\")");
            result.put("aciklama", "1 SQL: book + author + reviews WHERE id = " + bookId);
            result.put("book", b.getTitle());
            result.put("yazar", b.getAuthor().getName());
            result.put("reviews", b.getReviews().stream().map(r -> r.getRating() + "/5 - " + r.getContent()).toList());
            return result;
        }).orElse(Map.of("hata", "Kitap bulunamadı: " + bookId));
    }

    // ─── SENARYO 7: Named @EntityGraph - withAll (tek kitap) ─────────────────
    // "Book.withAll" grafiği: author + reviews + tags
    //
    // Console'da göreceğin SQL:
    //   1x  SELECT DISTINCT b.*, a.*, r.*, t.*
    //       FROM books b
    //       LEFT OUTER JOIN authors a ON ...
    //       LEFT OUTER JOIN reviews r ON ...
    //       LEFT OUTER JOIN book_tags bt ON ...
    //       LEFT OUTER JOIN tags t ON ...
    //       WHERE b.id = ?
    @Transactional(readOnly = true)
    public Map<String, Object> scenarioNamedEntityGraphAll(Long bookId) {
        return bookRepository.findWithAllById(bookId).map(b -> {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("scenario", "@NamedEntityGraph(\"Book.withAll\")");
            result.put("aciklama", "1 SQL: book + author + reviews + tags WHERE id = " + bookId);
            result.put("book", b.getTitle());
            result.put("yazar", b.getAuthor().getName());
            result.put("reviews", b.getReviews().stream().map(r -> r.getRating() + "/5 - " + r.getContent()).toList());
            result.put("tags", b.getTags().stream().map(t -> t.getName()).toList());
            return result;
        }).orElse(Map.of("hata", "Kitap bulunamadı: " + bookId));
    }
}
