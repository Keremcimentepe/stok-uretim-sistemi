package com.proje.stokuretim.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.List;

@Data // Getter, Setter ve ToString metotlarını otomatik oluşturur
@Entity // Bu sınıfın bir veritabanı tablosu olduğunu belirtir
@Table(name = "urunler") // Veritabanındaki tablo adı 'urunler' olsun


public class Urun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL (Otomatik Artan) ID
    @Column(name = "urun_id")
    private Integer urunId;
    // @Transient: Bu alan veritabanında sütun olarak oluşmaz, sadece ekranda göstermek içindir.
    @Transient
    private String depoDagilimi;

    @Column(name = "urun_kodu", nullable = false, unique = true, length = 50)
    private String urunKodu;

    @Column(name = "urun_adi", nullable = false, length = 150)
    private String urunAdi;

    @Column(name = "tur", nullable = false, length = 20)
    private String tur; // 'Hammadde', 'Mamul' vs.

    @Column(name = "birim", length = 20)
    private String birim;

    @Column(name = "kritik_stok_seviyesi")
    private Integer kritikStokSeviyesi = 10; // Varsayılan değer

    // Senin tasarımında sonradan eklediğimiz trigger alanı
    @Column(name = "guncel_stok")
    private Double guncelStok = 0.0;
    @OneToMany(mappedBy = "urun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StokHareket> hareketler;

}
