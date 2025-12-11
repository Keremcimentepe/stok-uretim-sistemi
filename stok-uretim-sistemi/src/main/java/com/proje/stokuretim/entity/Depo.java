package com.proje.stokuretim.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "depolar")
public class Depo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depo_id")
    private Integer depoId;

    @Column(name = "depo_adi")
    private String depoAdi;

    private String adres;

    @Column(name = "yetkili_kisi")
    private String yetkiliKisi;
}