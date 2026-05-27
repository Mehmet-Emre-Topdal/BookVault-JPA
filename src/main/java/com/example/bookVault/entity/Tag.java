package com.example.bookVault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Book ile ManyToMany ilişkisi
// Tag tarafından "mappedBy" var → Book sahibi taraf (join table orada tanımlı)
@Entity
@Table(name = "tags")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // mappedBy = Book.tags alanına bak, join table orada tanımlı
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();
}
