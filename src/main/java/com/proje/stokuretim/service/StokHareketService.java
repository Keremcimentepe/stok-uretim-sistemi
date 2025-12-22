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
import org.springframework.transaction.annotation.Transactional;

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
    // StokHareketService.java



    @Transactional
    public void stokHareketKaydet(StokHareket hareket) {
        // 1. Eğer işlem 'Cikis' ise Kontrol Et
        if ("Cikis".equals(hareket.getIslemTuru())) {

            // Veritabanına sor: Bu depoda bu üründen kaç tane var?
            Double mevcutStok = stokHareketRepository.getDepodakiNetStok(
                    hareket.getDepo().getDepoId(),
                    hareket.getUrun().getUrunId()
            );

            // 2. Yetersiz Stok Kontrolü
            if (mevcutStok < hareket.getMiktar()) {
                throw new RuntimeException("Yetersiz Stok! " +
                        hareket.getDepo().getDepoAdi() + " deposunda sadece " +
                        mevcutStok + " adet " + hareket.getUrun().getUrunAdi() + " bulunmaktadır.");
            }
        }

        // Kontrolü geçtiyse kaydet (Trigger çalışır, genel stok güncellenir)
        stokHareketRepository.save(hareket);
    }
    // StokHareketService.java içine ekle:

    @org.springframework.transaction.annotation.Transactional // Ya hepsi olur ya hiçbiri (Güvenlik)
    public void transferYap(Integer cikisDepoId, Integer girisDepoId, Integer urunId, Double miktar, String userEmail) {
        Kullanici yapanKisi = kullaniciRepository.findByEmail(userEmail).orElseThrow();
        Urun urun = urunRepository.findById(urunId).orElseThrow();
        Depo cikisDepo = depoRepository.findById(cikisDepoId).orElseThrow();
        Depo girisDepo = depoRepository.findById(girisDepoId).orElseThrow();

        // 1. ÇIKIŞ HAREKETİ (Kaynak Depodan)
        StokHareket cikis = new StokHareket();
        cikis.setUrun(urun);
        cikis.setDepo(cikisDepo);
        cikis.setMiktar(miktar);
        cikis.setIslemTuru("Cikis"); // Trigger çalışıp stoğu düşecek (Genel stok)
        cikis.setAciklama("Transfer: " + girisDepo.getDepoAdi() + " deposuna gönderildi.");
        cikis.setKullanici(yapanKisi);
        cikis.setTarih(LocalDateTime.now());
        stokHareketRepository.save(cikis);

        // 2. GİRİŞ HAREKETİ (Hedef Depoya)
        StokHareket giris = new StokHareket();
        giris.setUrun(urun);
        giris.setDepo(girisDepo);
        giris.setMiktar(miktar);
        giris.setIslemTuru("Giris"); // Trigger çalışıp stoğu artıracak (Genel stok dengelenecek)
        giris.setAciklama("Transfer: " + cikisDepo.getDepoAdi() + " deposundan geldi.");
        giris.setKullanici(yapanKisi);
        giris.setTarih(LocalDateTime.now());
        stokHareketRepository.save(giris);
    }
}
