package com.proje.stokuretim.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roller") // Veritabanındaki tablo adı
@Data // Getter, Setter, toString metodlarını otomatik yazar
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan ID (Serial)
    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "rol_adi", nullable = false, length = 50)
    private String rolAdi;

    @Column(length = 255)
    private String aciklama;
}