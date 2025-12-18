package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.Depo;
import com.proje.stokuretim.repository.DepoRepository;
import com.proje.stokuretim.repository.StokHareketRepository; // Import eklendi
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor // Final olan tüm değişkenler için otomatik constructor oluşturur
public class DepoController {

    private final DepoRepository depoRepository;

    // DÜZELTME: @Autowired kaldırıldı, private final yapıldı.
    // Artık Lombok bunu da otomatik inject edecek.
    private final StokHareketRepository stokHareketRepository;

    // Depo Listesi
    @GetMapping("/depolar")
    public String depoListesi(Model model) {
        model.addAttribute("depoListesi", depoRepository.findAll());
        return "depolar";
    }

    @GetMapping("/depo-detay/{id}")
    public String depoDetay(@PathVariable Integer id, Model model) {
        // Depo bilgilerini bul
        Depo depo = depoRepository.findById(id).orElseThrow();

        // O deponun içindeki ürün sayılarını hesapla
        List<Object[]> stokListesi = stokHareketRepository.depodakiStokDurumu(id);

        model.addAttribute("depo", depo);
        model.addAttribute("stokListesi", stokListesi);

        return "depo_detay";
    }

    // Depo Ekleme Formu
    @GetMapping("/depo-ekle")
    public String depoEkleForm(Model model) {
        model.addAttribute("depo", new Depo());
        return "depo_form";
    }

    // Depoyu Kaydet
    @PostMapping("/depo-kaydet")
    public String depoKaydet(@ModelAttribute Depo depo) {
        depoRepository.save(depo);
        return "redirect:/depolar";
    }
}