package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.StokHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StokHareketRepository extends JpaRepository<StokHareket, Integer> {

    // 1. BELİRLİ BİR DEPODAKİ ÜRÜNLERİN STOK DURUMU (Depo Detay Sayfası İçin)
    // Hata veren kısım burasıydı, bu eklenince düzelir.
    @Query(value = "SELECT u.urun_adi, " +
            "SUM(CASE WHEN h.islem_turu = 'Giris' THEN h.miktar ELSE 0 END) - " +
            "SUM(CASE WHEN h.islem_turu = 'Cikis' THEN h.miktar ELSE 0 END) as mevcut_stok " +
            "FROM stok_hareketleri h " +
            "JOIN urunler u ON h.urun_id = u.urun_id " +
            "WHERE h.depo_id = :depoId " +
            "GROUP BY u.urun_adi", nativeQuery = true)
    List<Object[]> depodakiStokDurumu(@Param("depoId") Integer depoId);
    // StokHareketRepository.java içine ekle:

    // Belirli bir depo ve ürün için (Giriş Toplamı - Çıkış Toplamı) işlemini yapar.
    // COALESCE: Eğer hiç kayıt yoksa NULL yerine 0 döndür demektir.
    @Query("SELECT COALESCE(SUM(CASE WHEN h.islemTuru = 'Giris' THEN h.miktar ELSE 0 END) - " +
            "SUM(CASE WHEN h.islemTuru = 'Cikis' THEN h.miktar ELSE 0 END), 0) " +
            "FROM StokHareket h WHERE h.depo.depoId = :depoId AND h.urun.urunId = :urunId")
    Double getDepodakiNetStok(@Param("depoId") Integer depoId, @Param("urunId") Integer urunId);

    // 2. BELİRLİ BİR ÜRÜNÜN HANGİ DEPODA NE KADAR OLDUĞU (Ana Sayfa İçin)
    // Batarya sorunu ve "Merkez: 50 | Şube: 10" yazısı için bu lazım.
    @Query(value = "SELECT d.depo_adi, " +
            "SUM(CASE WHEN h.islem_turu = 'Giris' THEN h.miktar ELSE 0 END) - " +
            "SUM(CASE WHEN h.islem_turu = 'Cikis' THEN h.miktar ELSE 0 END) " +
            "FROM stok_hareketleri h " +
            "JOIN depolar d ON h.depo_id = d.depo_id " +
            "WHERE h.urun_id = :urunId " +
            "GROUP BY d.depo_adi", nativeQuery = true)
    List<Object[]> urunBazliStokDurumu(@Param("urunId") Integer urunId);

    // 3. Belirli bir ürünün hareket geçmişi (Eski kodundan kalma olabilir, dursun zararı yok)
    List<StokHareket> findByUrun_UrunIdOrderByTarihDesc(Integer urunId);
}