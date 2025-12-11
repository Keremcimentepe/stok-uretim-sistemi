package com.proje.stokuretim.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "stok_hareketleri")
public class StokHareket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hareket_id")
    private Integer hareketId;

    // Hangi Ürün?
    @ManyToOne
    @JoinColumn(name = "urun_id")
    private Urun urun;

    // Hangi Depo?
    @ManyToOne
    @JoinColumn(name = "depo_id")
    private Depo depo;

    // İşlemi Kim Yaptı?
    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;

    @Column(name = "islem_turu")
    private String islemTuru; // 'Giris' veya 'Cikis'

    private Double miktar;

    private String aciklama;

    private LocalDateTime tarih = LocalDateTime.now(); // Şimdiki zamanı otomatik alır
}