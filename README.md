# BookVault 📚

Bu proje, Spring Boot ve Spring Data JPA kullanılarak geliştirilmiş bir kitap yönetim sistemidir. Modern Java backend geliştirme pratiklerini ve veri tabanı ilişkilerini öğrenmek amacıyla oluşturulmuştur.

## 🎓 Bu Proje İle Öğrenilen JPA Konuları

Bu projeyi geliştirirken aşağıdaki JPA ve Hibernate konuları üzerinde derinlemesine pratik yapılmıştır:

*   **Temel Entity Eşleştirmeleri:** `@Entity`, `@Table`, `@Id`, `@Column` ve `@GeneratedValue` (SEQUENCE ve IDENTITY stratejileri) kullanımı.
*   **İlişki Yönetimi:** `@ManyToOne` ve `@OneToMany` ilişkileri, `FetchType.LAZY` ile performans optimizasyonu.
*   **Cascade ve Orphan Removal:** İlişkili nesnelerin otomatik silinmesi ve yönetilmesi (`cascade = CascadeType.ALL`, `orphanRemoval = true`).
*   **Veri Tabanı İndeksleme:** `@Table` içinde `@Index` tanımlayarak sorgu performansının artırılması.
*   **Enum Veri Tipleri:** `@Enumerated(EnumType.STRING)` ile enum değerlerinin veri tabanında string olarak saklanması.
*   **Spring Data JPA Auditing:** `@CreatedDate`, `@LastModifiedDate` ve `@EntityListeners(AuditingEntityListener.class)` kullanarak kayıt oluşturma ve güncelleme zamanlarının otomatik takibi.
*   **Lifecycle Callbacks:** `@PostLoad` anotasyonu ile veri tabanından veri çekildikten sonra entity üzerinde işlem yapma.
*   **Gelişmiş Sorgulama Teknikleri:**
    *   **Derived Query Methods:** Metot isimlerinden otomatik sorgu oluşturma (`findByTitleContaining`, vb.).
    *   **JPQL:** `@Query` anotasyonu ile custom nesne sorguları yazma.
    *   **Native Queries:** Veri tabanına özgü SQL sorguları çalıştırma.
*   **Pagination & Sorting:** Büyük veri setlerini `Pageable` ve `Page` arayüzleri ile sayfalama ve sıralama.
*   **Projections:** `BookSummary` gibi interface-based projection yapıları ile sadece ihtiyaç duyulan kolonları çekerek veri transferini optimize etme.
*   **Specifications (Criteria API):** `JpaSpecificationExecutor` kullanarak dinamik ve tip güvenli (type-safe) sorgular oluşturma.
*   **Transient Alanlar:** `@Transient` ile veri tabanında saklanmayan ancak uygulama içinde kullanılan alanların yönetimi.

## 🚀 Teknolojiler
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Databas
- **Lombok**

## 🛠️ Kurulum ve Çalıştırma

1. Projeyi klonlayın:
   ```bash
   git clone https://github.com/kullaniciadi/bookVault.git
   ```
2. Proje dizinine gidin:
   ```bash
   cd bookVault
   ```
3. Uygulamayı çalıştırın:
   ```bash
   ./mvnw spring-boot:run
   ```

## 📂 Proje Yapısı

- `entity`: Veri tabanı modelleri.
- `repository`: Veri erişim katmanı.
- `service`: İş mantığı (Business logic).
- `controller`: API uç noktaları.
- `projection`: Optimize edilmiş veri görünümleri.
- `specification`: Dinamik sorgu filtreleri.
