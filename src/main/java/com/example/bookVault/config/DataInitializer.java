package com.example.bookVault.config;

import com.example.bookVault.entity.*;
import com.example.bookVault.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Uygulama başlarken test verisi ekler (H2 in-memory, her restart'ta sıfırlanır)
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        // ── Yazarlar ──────────────────────────────────────────────────────────
        Author orwell = new Author();
        orwell.setName("George Orwell");

        Author tolkien = new Author();
        tolkien.setName("J.R.R. Tolkien");

        Author woolf = new Author();
        woolf.setName("Virginia Woolf");

        authorRepository.save(orwell);
        authorRepository.save(tolkien);
        authorRepository.save(woolf);

        // ── Etiketler (Tag) ───────────────────────────────────────────────────
        Tag dystopia = new Tag(null, "Distopya", null);
        Tag fantasy = new Tag(null, "Fantezi", null);
        Tag classic = new Tag(null, "Klasik", null);
        Tag political = new Tag(null, "Politik", null);

        tagRepository.save(dystopia);
        tagRepository.save(fantasy);
        tagRepository.save(classic);
        tagRepository.save(political);

        // ── Kitaplar ──────────────────────────────────────────────────────────
        Book b1984 = new Book();
        b1984.setTitle("1984");
        b1984.setIsbn("9780451524935");
        b1984.setPageCount(328);
        b1984.setAuthor(orwell);
        b1984.setStatus(BookStatus.AVAILABLE);
        b1984.getTags().add(dystopia);
        b1984.getTags().add(political);
        b1984.getTags().add(classic);
        bookRepository.save(b1984);

        Book animalFarm = new Book();
        animalFarm.setTitle("Animal Farm");
        animalFarm.setIsbn("9780451526342");
        animalFarm.setPageCount(112);
        animalFarm.setAuthor(orwell);
        animalFarm.setStatus(BookStatus.AVAILABLE);
        animalFarm.getTags().add(political);
        animalFarm.getTags().add(classic);
        bookRepository.save(animalFarm);

        Book lotr = new Book();
        lotr.setTitle("The Lord of the Rings");
        lotr.setIsbn("9780618640157");
        lotr.setPageCount(1178);
        lotr.setAuthor(tolkien);
        lotr.setStatus(BookStatus.AVAILABLE);
        lotr.getTags().add(fantasy);
        lotr.getTags().add(classic);
        bookRepository.save(lotr);

        Book hobbit = new Book();
        hobbit.setTitle("The Hobbit");
        hobbit.setIsbn("9780547928227");
        hobbit.setPageCount(310);
        hobbit.setAuthor(tolkien);
        hobbit.setStatus(BookStatus.BORROWED);
        hobbit.getTags().add(fantasy);
        bookRepository.save(hobbit);

        Book waves = new Book();
        waves.setTitle("The Waves");
        waves.setIsbn("9780156949606");
        waves.setPageCount(229);
        waves.setAuthor(woolf);
        waves.setStatus(BookStatus.AVAILABLE);
        waves.getTags().add(classic);
        bookRepository.save(waves);

        // ── Yorumlar (Review) ─────────────────────────────────────────────────
        reviewRepository.save(new Review(null, "Distopik kurgunun başyapıtı. Herkese okunmalı.", 5, b1984));
        reviewRepository.save(new Review(null, "Günümüzle çarpıcı benzerlikler.", 5, b1984));
        reviewRepository.save(new Review(null, "Biraz ağır ama etkileyici.", 4, b1984));

        reviewRepository.save(new Review(null, "Kısa ama çok düşündürücü.", 5, animalFarm));
        reviewRepository.save(new Review(null, "Alegori şahane işlenmiş.", 4, animalFarm));

        reviewRepository.save(new Review(null, "Epik fantezinin zirvesi. Devasa ama değer.", 5, lotr));
        reviewRepository.save(new Review(null, "İkinci ciltten itibaren sürükleyici.", 4, lotr));

        reviewRepository.save(new Review(null, "Giriş niteliğinde mükemmel.", 5, hobbit));

        System.out.println("✓ Test verisi yüklendi: 3 yazar, 5 kitap, 4 tag, 8 review");
        System.out.println("✓ Test endpointleri: GET /fetch-demo/[n-plus-one|join-fetch|join-fetch-reviews|entity-graph-inline|entity-graph-all]");
        System.out.println("✓ Tek kitap: GET /fetch-demo/named-graph/{id} veya /fetch-demo/named-graph-all/{id}");
    }
}
