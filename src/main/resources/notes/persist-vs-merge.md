# persist vs merge

`save()` metodu içinde JPA iki farklı yol izler:

## persist()
- Entity'nin ID'si `null` ise çağrılır
- Entity'yi **yeni** kabul eder → INSERT üretir
- Entity o andan itibaren **Managed** (yönetilen) state'e girer

## merge()
- Entity'nin ID'si dolu ise çağrılır
- Entity'yi **mevcut** kabul eder → UPDATE üretir
- Detached (bağlı olmayan) entity'leri tekrar yönetim altına alır
- **Dikkat:** merge() orijinal objeyi değil, yeni bir managed kopya döner

NOT: BEN BU İKİSİNİN FARKINI ANLAMADIM, - **Dikkat:** merge() orijinal objeyi değil, yeni bir managed kopya döner VE - Detached (bağlı olmayan) entity'leri tekrar yönetim altına alır NE DEMEK  BİLEMİYORUM

## Özet
| | persist() | merge() |
|---|---|---|
| Ne zaman | ID null | ID dolu |
| SQL | INSERT | UPDATE |
| Seviye 4'te detaylı işlenecek | ✓ | ✓ |
