package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.service.UrunService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UrunController {
    // UrunController.java içine ekle:
    @GetMapping("/urun-sil/{id}") // URL'den ID'yi alır (Örn: /urun-sil/5)
    public String urunSil(@PathVariable("id") Integer id) {
        urunService.urunSil(id);
        return "redirect:/urunler"; // Listeyi yenile
    }
    @GetMapping("/urun-duzenle/{id}")
    public String urunDuzenleFormu(@PathVariable("id") Integer id, Model model) {
        Urun bulunanUrun = urunService.urunBul(id);
        model.addAttribute("urun", bulunanUrun); // Dolu nesneyi forma gönder
        return "urun_form"; // Aynı formu yeniden kullanıyoruz!
    }

    // 1. "Yeni Ekle" butonuna basılınca boş form sayfasını açar
    @GetMapping("/urun-ekle")
    public String yeniUrunFormu(Model model) {
        Urun yeniUrun = new Urun();
        yeniUrun.setKritikStokSeviyesi(10); // Varsayılan değer
        model.addAttribute("urun", yeniUrun); // Formun içine boş bir nesne gönderiyoruz
        return "urun_form"; // urun_form.html sayfasını aç
    }

    // 2. Formdaki "Kaydet" butonuna basılınca burası çalışır
    // UrunController.java içindeki urunKaydet metodu:

    // UrunController.java içindeki metot:

    @PostMapping("/urun-kaydet")
    public String urunKaydet(@ModelAttribute("urun") Urun urun, Principal principal) {

        if (urun.getUrunId() != null && urun.getUrunId() > 0) {
            // ID varsa bu bir GÜNCELLEME işlemidir.
            // Stok miktarını ellemeden diğer bilgileri güncelle.
            urunService.urunGuncelle(urun);
        } else {
            // ID yoksa bu YENİ KAYIT işlemidir.
            // Yeni ürün ekleme servisini çağır (Otomatik Merkez Depo girişi yapan).
            urunService.yeniUrunEkle(urun, principal.getName());
        }

        return "redirect:/urunler";
    }
    @GetMapping("/urunler")
    public String urunListesi(Model model) {
        // GÜNCELLENEN SATIR:
        List<Urun> urunler = urunService.urunleriDetayliGetir();

        model.addAttribute("urunListesi", urunler);
        model.addAttribute("kritikSayi", urunService.kritikStokSayisi());
        return "urunler";
    }


    private final UrunService urunService;


}
