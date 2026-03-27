package com.example.bookVault.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @Column(unique = true /*burada index oluşturuluyor */, length = 13)
    private String isbn;

    @Column(name = "page_count")
    private int pageCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
     private Author author;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookStatus status; // Durum bilgisi (AVAILABLE, BORROWED, LOST)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Transient
    private String displayLabel; // title + " - " + author

    @PostLoad
    protected void onLoad() {
        //entiyy db den yüklenince burası çalışır
        this.displayLabel = title + " - " + (author != null ? author.getName() : "");
    }
}
