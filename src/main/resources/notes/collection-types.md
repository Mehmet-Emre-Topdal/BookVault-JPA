# JPA'da Collection Tipleri

## List vs Set vs ArrayList

`List<Book>` bir interface'dir — `ArrayList`, `LinkedList` gibi implementasyonları var.

JPA ilişkilerinde `List<Book>` yazsan da Hibernate bunu **PersistentBag** adlı kendi proxy nesnesiyle değiştirir.
Yani `new ArrayList<>()` yazsan bile Hibernate onu kendi nesnesiyle ezar — ama yazmak yine de iyi pratiktir:

```java
// Önerilen
@OneToMany(mappedBy = "author")
private List<Book> books = new ArrayList<>(); // null'dan korur, Hibernate ezar ama sorun olmaz
```

## Neden List tercih edilir?

| | List | Set |
|---|---|---|
| Duplicate | İzin verir | İzin vermez |
| Sıralama | Korunur | Korunmaz |
| Performans | Daha hızlı | equals/hashCode gerektirir |

- `@OneToMany` → genellikle `List` yeterli
- `@ManyToMany` → `Set` daha mantıklı (duplicate category istemezsin)

## NOT: Tam anlamadım
Hibernate neden kendi proxy list'ini kullanır?
→ Lazy loading için. `books` alanına ilk erişildiğinde Hibernate araya girerek SQL atar.
  Bunun için listeyi kendi kontrolündeki bir nesneyle sarmalıyor.
→ Lazy vs Eager loading konusunda (bir sonraki konu) daha netleşecek.
