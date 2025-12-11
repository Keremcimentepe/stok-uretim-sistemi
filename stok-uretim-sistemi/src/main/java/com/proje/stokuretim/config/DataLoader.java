package com.proje.stokuretim.config;

import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.entity.Rol;
import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.entity.Depo;
import com.proje.stokuretim.repository.KullaniciRepository;
import com.proje.stokuretim.repository.RolRepository;
import com.proje.stokuretim.repository.UrunRepository;
import com.proje.stokuretim.repository.DepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final KullaniciRepository kullaniciRepository;
    private final UrunRepository urunRepository;
    private final DepoRepository depoRepository;
    private final PasswordEncoder passwordEncoder;

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

        // 3. DEPO EKLE (YENİ KISIM)
        if (depoRepository.count() == 0) {
            Depo anaDepo = new Depo();
            anaDepo.setDepoAdi("Merkez Depo");
            anaDepo.setAdres("Organize Sanayi Bölgesi");
            anaDepo.setYetkiliKisi("Ahmet Şef");
            depoRepository.save(anaDepo);

            System.out.println("--- MERKEZ DEPO EKLENDİ ---");
        }

        // 4. Örnek ÜRÜNLERİ ekle
        if (urunRepository.count() == 0) {
            Urun hammadde = new Urun();
            hammadde.setUrunAdi("Demir Plaka 5mm");
            hammadde.setUrunKodu("HMD-001");
            hammadde.setTur("Hammadde");
            hammadde.setBirim("Adet");
            hammadde.setKritikStokSeviyesi(50);
            hammadde.setGuncelStok(100.0);
            urunRepository.save(hammadde);

            Urun mamul = new Urun();
            mamul.setUrunAdi("Çelik Jant 16 inç");
            mamul.setUrunKodu("MML-101");
            mamul.setTur("Mamul");
            mamul.setBirim("Adet");
            mamul.setKritikStokSeviyesi(10);
            mamul.setGuncelStok(20.0);
            urunRepository.save(mamul);

            System.out.println("--- ÖRNEK ÜRÜNLER EKLENDİ ---");
        }
    }
}