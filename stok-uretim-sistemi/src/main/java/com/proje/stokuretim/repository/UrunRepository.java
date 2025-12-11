package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.Urun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.proje.stokuretim.entity.Urun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrunRepository extends JpaRepository<Urun, Integer> {
    // JpaRepository sayesinde save(), findAll(), delete() gibi metotlar hazır gelir.

    // Özel sorgu gerekirse buraya metod ismini yazmak yeterli.
    // Örnek: Ürün koduna göre bulmak istersek:
    Optional<Urun> findByUrunKodu(String urunKodu);
    // Stored Procedure Çağırma Yöntemi
    @Procedure(procedureName = "sp_kritik_stok_sayisi")
    Integer kritikStokSayisiniGetir();
}

