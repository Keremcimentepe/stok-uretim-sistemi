package com.proje.stokuretim.service;

import com.proje.stokuretim.entity.Depo;
import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.entity.StokHareket;
import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.repository.DepoRepository;
import com.proje.stokuretim.repository.KullaniciRepository;
import com.proje.stokuretim.repository.StokHareketRepository;
import com.proje.stokuretim.repository.UrunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StokHareketService {

    private final StokHareketRepository stokHareketRepository;
    private final UrunRepository urunRepository;
    private final DepoRepository depoRepository;
    private final KullaniciRepository kullaniciRepository;

    // Dropdownlar için listeler
    public List<Urun> tumUrunler() { return urunRepository.findAll(); }
    public List<Depo> tumDepolar() { return depoRepository.findAll(); }

    // Hareketi Kaydetme İşlemi
    public void hareketKaydet(StokHareket hareket, String kullaniciEmail) {
        // İşlemi yapan kullanıcıyı bul ve harekete ekle
        Kullanici yapanKisi = kullaniciRepository.findByEmail(kullaniciEmail).orElseThrow();
        hareket.setKullanici(yapanKisi);

        hareket.setTarih(LocalDateTime.now()); // Şu anki zaman
        stokHareketRepository.save(hareket);
    }
}