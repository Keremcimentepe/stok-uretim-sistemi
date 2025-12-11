package com.proje.stokuretim.service;

import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.repository.UrunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Spring'e bunun bir servis olduğunu söyler
@RequiredArgsConstructor
public class UrunService {
    @Transactional // Procedure çağırırken Transactional olması gerekir
    public Integer kritikStokSayisi() {
        return urunRepository.kritikStokSayisiniGetir();
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

    private final UrunRepository urunRepository;

    // Tüm ürünleri getiren metot
    public List<Urun> tumUrunleriGetir() {
        return urunRepository.findAll();
    }

    // İleride buraya 'urunKaydet', 'urunSil' gibi metotlar da ekleyeceğiz.
}
