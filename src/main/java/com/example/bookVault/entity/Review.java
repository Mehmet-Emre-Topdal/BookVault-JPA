package com.example.bookVault.entity;

import jakarta.persistence.*;
import lombok.*;

// Book ile ManyToOne ilişkisi - LAZY (varsayılan)
// Review tarafından bakınca: her review bir kitaba ait
// Book tarafından bakınca: bir kitabın birden fazla review'u var
@Entity
@Table(name = "reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private int rating; // 1-5

    // LAZY: Review yüklenince book otomatik çekilmez
    // book'a erişildiği anda ayrı bir SELECT çalışır
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
