package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.entity.StokHareket;
import com.proje.stokuretim.repository.KullaniciRepository; // Import Eklendi
import com.proje.stokuretim.service.StokHareketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class StokHareketController {

    // İsim düzeltmesi: Aşağıda 'service' diye kullanacağız
    private final StokHareketService service;

    // EKSİK OLAN KISIM: Repository buraya eklendi, Lombok otomatik inject edecek
    private final KullaniciRepository kullaniciRepository;

    @GetMapping("/stok-hareket")
    public String hareketFormu(Model model) {
        model.addAttribute("hareket", new StokHareket());
        model.addAttribute("urunListesi", service.tumUrunler());
        model.addAttribute("depoListesi", service.tumDepolar());
        return "stok_hareket_form";
    }

    @PostMapping("/stok-hareket-kaydet")
    public String stokHareketKaydet(@ModelAttribute StokHareket stokHareket,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Artık kullaniciRepository tanınıyor ✅
            Kullanici user = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
            stokHareket.setKullanici(user);
            stokHareket.setTarih(LocalDateTime.now());

            // İsim düzeltildi: 'stokHareketService' yerine 'service' yazıldı ✅
            service.stokHareketKaydet(stokHareket);

            redirectAttributes.addFlashAttribute("success", "Stok hareketi başarıyla kaydedildi.");

        } catch (RuntimeException e) {
            // "Yetersiz Stok" hatası gelirse yakala ve ekrana bas
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/stok-hareket";
    }

    @GetMapping("/transfer")
    public String transferFormu(Model model) {
        model.addAttribute("urunListesi", service.tumUrunler());
        model.addAttribute("depoListesi", service.tumDepolar());
        return "transfer_form";
    }

    @PostMapping("/transfer-yap")
    public String transferYap(
            @RequestParam Integer cikisDepoId,
            @RequestParam Integer girisDepoId,
            @RequestParam Integer urunId,
            @RequestParam Double miktar,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            // Servis artık kontrol yapıyor
            service.transferYap(cikisDepoId, girisDepoId, urunId, miktar, principal.getName());

            redirectAttributes.addFlashAttribute("success", "Transfer başarıyla gerçekleşti.");
            return "redirect:/depolar"; // Başarılıysa Depolara git

        } catch (RuntimeException e) {
            // HATA VARSA: Transfer sayfasına geri dön ve hatayı göster
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/transfer";
        }
    }
}