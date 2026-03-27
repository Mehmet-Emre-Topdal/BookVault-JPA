# N+1 Problemi ve Çözümleri

## Problem

`findAll()` ile entity listesi çekilip döngüde ilişkili entity'e erişilince N+1 sorgu oluşur.

```java
List<Book> books = bookRepository.findAll();
books.forEach(b -> System.out.println(b.getAuthor().getName()));
```

```sql
SELECT * FROM books;                   -- 1 sorgu
SELECT * FROM authors WHERE id = 1;   -- her kitap için ayrı sorgu
SELECT * FROM authors WHERE id = 2;
-- 100 kitap → 101 sorgu
```

---

## Çözüm 1: JOIN FETCH (JPQL)

```java
@Query("SELECT b FROM Book b JOIN FETCH b.author")
List<Book> findAllWithAuthor();
```

Tek sorguda hem books hem authors çekilir:
```sql
SELECT b.*, a.* FROM books b JOIN authors a ON b.author_id = a.id
```

---

## Çözüm 2: @EntityGraph

```java
@EntityGraph(attributePaths = {"author"})
List<Book> findAll();
```

JOIN FETCH ile aynı sonucu verir. JPQL yazmaya gerek yoktur.

---

## LAZY vs EAGER — Doğru Strateji

| Yaklaşım | Davranış | Sorun |
|---|---|---|
| `FetchType.EAGER` | Her sorguda JOIN yapılır | Gereksiz veri çekilir |
| `FetchType.LAZY` (default) | İlişki gerekince yüklenir | Döngüde erişilirse N+1 oluşur |
| **LAZY + JOIN FETCH** | Gerektiğinde tek sorguda yüklenir | **Doğru yaklaşım** |

**Kural:** İlişkileri her zaman `FetchType.LAZY` tanımla.
Döngüde ya da toplu işlemde ilişkiye erişeceksen `JOIN FETCH` veya `@EntityGraph` kullan.
