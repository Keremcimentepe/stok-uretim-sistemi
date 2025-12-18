package com.proje.stokuretim.config;

import com.proje.stokuretim.entity.*;
import com.proje.stokuretim.repository.KullaniciRepository;
import com.proje.stokuretim.repository.RolRepository;
import com.proje.stokuretim.repository.UrunRepository;
import com.proje.stokuretim.repository.DepoRepository;
import com.proje.stokuretim.repository.StokHareketRepository; // Doğru import
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final KullaniciRepository kullaniciRepository;
    private final UrunRepository urunRepository;
    private final DepoRepository depoRepository;
    private final StokHareketRepository stokHareketRepository; // EKSİKTİ, EKLENDİ
    private final PasswordEncoder passwordEncoder;

    // NOT: @Query buraya YAZILMAZ. O kısım StokHareketRepository içinde kalmalı.

    @Override
    public void run(String... args) throws Exception {

        // 1. Önce ROLLERİ kontrol et, yoksa ekle
        if (rolRepository.count() == 0) {
            Rol adminRol = new Rol();
            adminRol.setRolAdi("ADMIN");
            adminRol.setAciklama("Tam Yetkili Yönetici");
            rolRepository.save(adminRol);

            Rol depoRol = new Rol();
            depoRol.setRolAdi("DEPO_SORUMLUSU");
            depoRol.setAciklama("Sadece Stok Giriş/Çıkış Yapabilir");
            rolRepository.save(depoRol);

            System.out.println("--- ROLLER EKLENDİ ---");
        }

        // 2. KULLANICILARI ekle
        if (kullaniciRepository.count() == 0) {
            Rol adminRol = rolRepository.findByRolAdi("ADMIN").orElseThrow();

            Kullanici adminUser = new Kullanici();
            adminUser.setAdSoyad("Proje Admin");
            adminUser.setEmail("proje@admin.com");
            adminUser.setSifre(passwordEncoder.encode("12345"));
            adminUser.setRol(adminRol);
            adminUser.setAktifMi(true);

            kullaniciRepository.save(adminUser);
            System.out.println("--- ADMIN KULLANICISI EKLENDİ (Şifreli) ---");
        }

        // 3. DEPO EKLE
        if (depoRepository.count() == 0) {
            Depo anaDepo = new Depo();
            anaDepo.setDepoAdi("Merkez Depo");
            anaDepo.setAdres("Organize Sanayi Bölgesi");
            anaDepo.setYetkiliKisi("Ahmet Şef");
            depoRepository.save(anaDepo);

            System.out.println("--- MERKEZ DEPO EKLENDİ ---");
        }

        // 4. Örnek ÜRÜNLERİ ve İLK STOKLARI Ekle
        if (urunRepository.count() == 0) {
            // Önce Ana Depoyu Bul
            Depo anaDepo = depoRepository.findAll().get(0);
            Rol adminRol = rolRepository.findByRolAdi("ADMIN").orElseThrow();
            Kullanici adminUser = kullaniciRepository.findByEmail("proje@admin.com").orElseThrow();

            // Ürün 1: Demir Plaka
            Urun hammadde = new Urun();
            hammadde.setUrunAdi("Demir Plaka 5mm");
            hammadde.setUrunKodu("HMD-001");
            hammadde.setTur("Hammadde");
            hammadde.setBirim("Adet");
            hammadde.setKritikStokSeviyesi(50);
            hammadde.setGuncelStok(100.0);
            urunRepository.save(hammadde);

            // STOK HAREKETİ 1 (Merkez Depoya Giriş)
            StokHareket h1 = new StokHareket();
            h1.setUrun(hammadde);
            h1.setDepo(anaDepo);
            h1.setMiktar(100.0);
            h1.setIslemTuru("Giris");
            h1.setAciklama("Açılış stoğu");
            h1.setKullanici(adminUser);
            h1.setTarih(LocalDateTime.now());
            stokHareketRepository.save(h1);

            // Ürün 2: Batarya
            Urun mamul = new Urun();
            mamul.setUrunAdi("Lityum Batarya");
            mamul.setUrunKodu("BAT-101");
            mamul.setTur("Mamul");
            mamul.setBirim("Adet");
            mamul.setKritikStokSeviyesi(10);
            mamul.setGuncelStok(50.0);
            urunRepository.save(mamul);

            // STOK HAREKETİ 2 (Merkez Depoya Giriş)
            StokHareket h2 = new StokHareket();
            h2.setUrun(mamul);
            h2.setDepo(anaDepo);
            h2.setMiktar(50.0);
            h2.setIslemTuru("Giris");
            h2.setAciklama("Açılış stoğu");
            h2.setKullanici(adminUser);
            h2.setTarih(LocalDateTime.now());
            stokHareketRepository.save(h2);

            System.out.println("--- ÜRÜNLER VE İLK STOK HAREKETLERİ EKLENDİ ---");
        }
    }
}