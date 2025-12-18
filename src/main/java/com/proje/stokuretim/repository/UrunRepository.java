package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.Urun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure; // Bu import önemli!
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrunRepository extends JpaRepository<Urun, Integer> {

    // 1. STORED PROCEDURE (Geri getirdik ✅)
    // Veritabanındaki 'sp_kritik_stok_sayisi' prosedürünü çağırır
    @Procedure(procedureName = "sp_kritik_stok_sayisi")
    Integer kritikStokSayisiniGetir();

    // 2. ARAMA SORGUSU (Hatayı çözen garantili yöntem ✅)
    // Hem ada hem koda bakar, büyük/küçük harf fark etmez
    @Query("SELECT u FROM Urun u WHERE LOWER(u.urunAdi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.urunKodu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Urun> aramaYap(@Param("keyword") String keyword);
}