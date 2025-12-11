package com.proje.stokuretim.controller;

import com.proje.stokuretim.entity.StokHareket;
import com.proje.stokuretim.service.StokHareketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StokHareketController {

    private final StokHareketService service;

    @GetMapping("/stok-hareket")
    public String hareketFormu(Model model) {
        model.addAttribute("hareket", new StokHareket());
        model.addAttribute("urunListesi", service.tumUrunler());
        model.addAttribute("depoListesi", service.tumDepolar());
        return "stok_hareket_form";
    }

    @PostMapping("/stok-hareket-kaydet")
    public String hareketiKaydet(@ModelAttribute("hareket") StokHareket hareket, Principal principal) {
        // Principal: O an sisteme giriş yapmış kullanıcının bilgisini tutar
        service.hareketKaydet(hareket, principal.getName());
        return "redirect:/urunler"; // İşlem bitince ürün listesine dön
    }
}