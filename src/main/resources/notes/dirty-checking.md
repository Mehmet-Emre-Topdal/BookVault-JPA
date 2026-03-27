# Dirty Checking

Hibernate, transaction içinde yüklenen entity'leri snapshot olarak hafızaya alır.
Transaction bitiminde snapshot ile karşılaştırır, fark varsa otomatik UPDATE atar.
save() çağırmana gerek yok — @Transactional varsa yeterli.

## ⚠️ @Transactional olmadan çalışmaz

```java
// @Transactional YOK
public void updateTitle(Long id, String newTitle) {
    Book book = bookRepository.findById(id).get();
    book.setTitle(newTitle);
    // Transaction yok → flush yok → UPDATE ATILMAZ, sessizce geçer
}
```

Dirty checking'in çalışması için metodun @Transactional olması şart.

## readOnly = true ile kapatmak

Salt okunur sorgularda snapshot almak gereksiz maliyet:

```java
@Transactional(readOnly = true)
public List<Book> findAll() { ... }
```
