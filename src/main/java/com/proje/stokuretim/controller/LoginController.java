package com.proje.stokuretim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String girisSayfasi() {
        // templates klasöründeki login.html dosyasını bulup ekrana basar
        return "login";
    }
}