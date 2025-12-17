package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.repository.KullaniciRepository;
import com.proje.stokuretim.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class KullaniciController {

    private final KullaniciRepository kullaniciRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/kullanici-ekle")
    public String kullaniciEkleForm(Model model) {
        model.addAttribute("kullanici", new Kullanici());
        model.addAttribute("roller", rolRepository.findAll());
        return "kullanici_form";
    }

    @PostMapping("/kullanici-kaydet")
    public String kullaniciKaydet(@ModelAttribute Kullanici kullanici) {
        // Şifreyi kriptolayıp kaydediyoruz
        kullanici.setSifre(passwordEncoder.encode(kullanici.getSifre()));
        kullanici.setAktifMi(true);
        kullaniciRepository.save(kullanici);
        return "redirect:/urunler"; // Şimdilik ana sayfaya dönsün
    }
}