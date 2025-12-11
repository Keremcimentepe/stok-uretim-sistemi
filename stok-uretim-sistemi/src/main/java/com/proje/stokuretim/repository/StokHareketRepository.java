package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.StokHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StokHareketRepository extends JpaRepository<StokHareket, Integer> {
    // Belirli bir ürünün hareketlerini listelemek için:
    List<StokHareket> findByUrun_UrunIdOrderByTarihDesc(Integer urunId);
}