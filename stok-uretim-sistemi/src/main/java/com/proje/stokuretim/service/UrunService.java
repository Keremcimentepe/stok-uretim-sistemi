package com.proje.stokuretim.service;

import com.proje.stokuretim.entity.Depo;
import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.entity.StokHareket;
import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.repository.*;
import lombok.RequiredArgsConstructor;
import com.proje.stokuretim.repository.StokHareketRepository; // Doğru import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service // Spring'e bunun bir servis olduğunu söyler
@RequiredArgsConstructor
public class UrunService {
    @Transactional // Procedure çağırırken Transactional olması gerekir
    public Integer kritikStokSayisi() {
        return urunRepository.kritikStokSayisiniGetir();
    }
    // UrunService.java içine ekle:

    @Transactional
    public void urunGuncelle(Urun gelenUrun) {
        // 1. Veritabanındaki asıl ürünü bul
        Urun mevcutUrun = urunRepository.findById(gelenUrun.getUrunId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        // 2. SADECE değişmesine izin verdiğimiz alanları güncelle
        mevcutUrun.setUrunAdi(gelenUrun.getUrunAdi());
        mevcutUrun.setUrunKodu(gelenUrun.getUrunKodu());
        mevcutUrun.setTur(gelenUrun.getTur());
        mevcutUrun.setBirim(gelenUrun.getBirim());
        mevcutUrun.setKritikStokSeviyesi(gelenUrun.getKritikStokSeviyesi());

        // DİKKAT: setGuncelStok() çağrılmıyor!
        // Böylece stok veritabanında neyse o kalıyor.

        urunRepository.save(mevcutUrun);
    }

    // UrunService.java içine ekle:
    // ID'si verilen ürünü siler
    public void urunSil(Integer id) {
        urunRepository.deleteById(id);
    }
    // ID'ye göre tek bir ürün getirir
    public Urun urunBul(Integer id) {
        return urunRepository.findById(id).orElse(null);
    }

    public void urunKaydet(Urun urun) {
        // İleride burada "Bu ürün kodu daha önce kullanılmış mı?" gibi kontroller de yapacağız.
        urunRepository.save(urun);
    }
    // UrunService.java içine ekle:

    @Autowired
    private DepoRepository depoRepository; // Depo bulmak için lazım

    @Autowired
    private KullaniciRepository kullaniciRepository; // İşlemi kim yaptı?

    @Transactional // Hata olursa işlemi geri al (Rollback)
    public void yeniUrunEkle(Urun urun, String kullaniciEmail) {
        // 1. Kullanıcının girdiği ilk stok miktarını al
        Double ilkStok = urun.getGuncelStok();

        // 2. Ürünü önce stoğu 0 olarak kaydet (Trigger çakışmasın diye)
        urun.setGuncelStok(0.0);
        Urun kaydedilenUrun = urunRepository.save(urun);

        // 3. Eğer kullanıcı 0'dan büyük bir stok girdiyse, Hareket oluştur
        if (ilkStok != null && ilkStok > 0) {
            StokHareket hareket = new StokHareket();
            hareket.setUrun(kaydedilenUrun);

            // Varsayılan olarak ID'si 1 olan depoyu (Merkez Depo) seçiyoruz
            // (Veritabanında ID'si 1 olan deponun silinmediğinden emin ol)
            Depo merkezDepo = depoRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Merkez Depo bulunamadı!"));

            hareket.setDepo(merkezDepo);
            hareket.setMiktar(ilkStok);
            hareket.setIslemTuru("Giris"); // Trigger çalışıp ana stoğu artıracak
            hareket.setAciklama("Yeni ürün açılış kaydı");

            // İşlemi yapan kullanıcı
            Kullanici user = kullaniciRepository.findByEmail(kullaniciEmail).orElseThrow();
            hareket.setKullanici(user);
            hareket.setTarih(LocalDateTime.now());

            stokHareketRepository.save(hareket);
        }
    }

    private final UrunRepository urunRepository;

    // Tüm ürünleri getiren metot
    public List<Urun> tumUrunleriGetir() {
        return urunRepository.findAll();
    }
    @Autowired
    private StokHareketRepository stokHareketRepository;

    public List<Urun> urunleriDetayliGetir() {
        List<Urun> urunler = urunRepository.findAll();

        for (Urun u : urunler) {
            // Veritabanından [DepoAdi, Miktar] listesini çek
            List<Object[]> stoklar = stokHareketRepository.urunBazliStokDurumu(u.getUrunId());

            // Metni oluştur (Örn: "Merkez: 50 | Şube: 10")
            StringBuilder dagilim = new StringBuilder();
            for (Object[] row : stoklar) {
                String depoAdi = (String) row[0];
                Double miktar = ((Number) row[1]).doubleValue(); // Casting güvenliği
                if (miktar > 0) { // Sadece stoğu olan depoları göster
                    dagilim.append(depoAdi).append(": <b>").append(miktar).append("</b> | ");
                }

            }

            u.setDepoDagilimi(dagilim.toString());
        }

        return urunler;
    }

    // İleride buraya 'urunKaydet', 'urunSil' gibi metotlar da ekleyeceğiz.
}
