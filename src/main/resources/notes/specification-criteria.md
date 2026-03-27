# Specification: (root, query, cb) nedir?

`Specification<T>` aslında şu functional interface'i implement eder:

```java
Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
```

Lambda ile yazdığında 3 parametre gelir:

---

## root
Sorguladığın entity'nin alanlarına erişim noktası.
Tablo'nun kendisi gibi düşün — kolonlara buradan ulaşırsın.

```java
root.get("title")      // books.title
root.get("pageCount")  // books.page_count
root.join("author")    // author tablosuna JOIN
```

---

## query
Sorgunun kendisi — SELECT, DISTINCT, ORDER BY gibi işlemler buradan yapılır.
Çoğu zaman kullanılmaz, özel durumlarda lazım olur.

```java
query.distinct(true);  // SELECT DISTINCT
```

---

## cb (CriteriaBuilder)
Koşulları (Predicate) üretir — WHERE içindeki ifadeler buradan gelir.

```java
cb.equal(root.get("title"), "Clean Code")           // title = 'Clean Code'
cb.like(root.get("title"), "%clean%")               // title LIKE '%clean%'
cb.greaterThan(root.get("pageCount"), 200)          // page_count > 200
cb.and(predicate1, predicate2)                      // AND
cb.or(predicate1, predicate2)                       // OR
cb.isNull(root.get("isbn"))                         // isbn IS NULL
```

---

## Tam örnek — projede kullanılmayan `query` ile

```java
// Bir yazarın kitaplarını çekerken duplicate gelmesin (JOIN nedeniyle olabilir)
public static Specification<Book> distinctByAuthor(String authorName) {
    return (root, query, cb) -> {
        query.distinct(true);  // <-- query burada kullanıldı
        Join<Book, Author> author = root.join("author", JoinType.LEFT);
        return cb.equal(author.get("name"), authorName);
    };
}
```

---

## Özet

| Parametre | Ne işe yarar | Ne zaman kullanılır |
|---|---|---|
| `root` | Entity alanlarına erişim | Her zaman |
| `query` | SELECT seviyesinde kontrol | DISTINCT, subquery gerekince |
| `cb` | WHERE koşulları üretir | Her zaman |
