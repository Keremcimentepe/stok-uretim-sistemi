package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.Urun;
import com.proje.stokuretim.service.UrunService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UrunController {

    private final UrunService urunService;

    // 1. LİSTELEME VE ARAMA (Tek Metot Haline Getirildi)
    @GetMapping("/urunler")
    public String urunListesi(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Urun> urunler;

        // Eğer arama kelimesi varsa 'urunAra' servisini çağır
        if (keyword != null && !keyword.isEmpty()) {
            urunler = urunService.urunAra(keyword);
        } else {
            // Yoksa tüm ürünleri detaylı (depo dağılımıyla) getir
            urunler = urunService.urunleriDetayliGetir();
        }

        model.addAttribute("urunListesi", urunler);

        // Kritik Stok Uyarısı (Stored Procedure)
        model.addAttribute("kritikSayi", urunService.kritikStokSayisi());

        // Arama kutusunda kelime kalsın diye geri gönderiyoruz
        model.addAttribute("keyword", keyword);

        return "urunler";
    }

    // 2. YENİ ÜRÜN EKLEME FORMU
    @GetMapping("/urun-ekle")
    public String yeniUrunFormu(Model model) {
        Urun yeniUrun = new Urun();
        yeniUrun.setKritikStokSeviyesi(10); // Varsayılan değer
        model.addAttribute("urun", yeniUrun);
        return "urun_form";
    }

    // 3. ÜRÜN DÜZENLEME FORMU
    @GetMapping("/urun-duzenle/{id}")
    public String urunDuzenleFormu(@PathVariable("id") Integer id, Model model) {
        Urun bulunanUrun = urunService.urunBul(id);
        model.addAttribute("urun", bulunanUrun);
        return "urun_form";
    }

    // 4. KAYDETME (Hem Yeni Ekleme Hem Güncelleme)
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

    // 5. SİLME İŞLEMİ
    @GetMapping("/urun-sil/{id}")
    public String urunSil(@PathVariable("id") Integer id) {
        urunService.urunSil(id);
        return "redirect:/urunler";
    }
}