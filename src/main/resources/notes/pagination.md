# Pagination & Sorting

## Page<T> yapısı

`Page<T>` döndüğünde JSON şu şekilde gelir:

```json
{
  "content": [...],         // bu sayfadaki kayıtlar
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": { "sorted": false }
  },
  "totalElements": 47,      // DB'deki toplam kayıt (COUNT sorgusu atar)
  "totalPages": 5,          // toplam sayfa sayısı
  "first": true,            // ilk sayfa mı
  "last": false,            // son sayfa mı
  "numberOfElements": 10,   // bu sayfada kaç kayıt var
  "empty": false
}
```

## Page vs Slice

| | Page | Slice |
|---|---|---|
| COUNT sorgusu | Evet (yavaş) | Hayır (hızlı) |
| totalElements | Var | Yok |
| Ne zaman | Sayfa numarası gösterilecekse | "Daha fazla yükle" butonu |

## Pageable + @Query nasıl çalışır?

Sen sadece JPQL'i yazarsın, Hibernate Pageable'ı görünce otomatik LIMIT/OFFSET ekler:

```java
@Query("SELECT b.title as title FROM Book b")
Page<BookSummary> findAllSummaries(Pageable pageable);
```

Hibernate üretir:
```sql
SELECT b.title FROM books LIMIT 10 OFFSET 0   -- veri
SELECT COUNT(*) FROM books                     -- totalElements için
```
