package com.example.bookVault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// ─── Named Entity Graph Tanımları ───────────────────────────────────────────
// Bu grafikler repository metodlarında @EntityGraph("isim") ile kullanılır.
// Sadece ihtiyaç duyulduğunda ilgili ilişkileri JOIN ile çeker.
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Book.withAuthor",
        attributeNodes = @NamedAttributeNode("author")
    ),
    @NamedEntityGraph(
        name = "Book.withAuthorAndReviews",
        attributeNodes = {
            @NamedAttributeNode("author"),
            @NamedAttributeNode("reviews")
        }
    ),
    @NamedEntityGraph(
        name = "Book.withAll",
        attributeNodes = {
            @NamedAttributeNode("author"),
            @NamedAttributeNode("reviews"),
            @NamedAttributeNode("tags")
        }
    )
})
@Entity
@Table(name = "books", indexes = {
    @Index(name="idx_books_author", columnList="author_id")
})
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(unique = true, length = 13)
    private String isbn;

    @Column(name = "page_count")
    private int pageCount;

    // LAZY: Book yüklenince author otomatik çekilmez.
    // author.getName() gibi bir erişim olursa ayrı SELECT çalışır.
    // DİKKAT: @PostLoad içinde author'a erişildiğinden, her Book yüklemesinde
    // author da LAZY proxy üzerinden yüklenir (session açıkken).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    // LAZY: Book listesi çekilince reviews otomatik gelmez.
    // N+1 problemi örneği için idealdir.
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // ManyToMany - LAZY: tags da otomatik çekilmez.
    // Ara tablo: book_tags (book_id, tag_id)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_tags",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Transient
    private String displayLabel;

    @PostLoad
    protected void onLoad() {
        this.displayLabel = title + " - " + (author != null ? author.getName() : "");
    }
}
