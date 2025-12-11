package com.proje.stokuretim.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kullanicilar")
@Data
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kullanici_id")
    private Integer kullaniciId;

    @Column(name = "ad_soyad", nullable = false, length = 100)
    private String adSoyad;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String sifre;

    // İLİŞKİ: Bir kullanıcı bir role sahiptir.
    // SQL'deki 'rol_id' sütununu Java nesnesine bağlıyoruz.
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Column(name = "aktif_mi")
    private Boolean aktifMi = true;
}