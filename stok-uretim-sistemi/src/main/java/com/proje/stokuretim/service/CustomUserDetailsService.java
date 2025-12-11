package com.proje.stokuretim.service;

import com.proje.stokuretim.entity.Kullanici;
import com.proje.stokuretim.entity.Rol;
import com.proje.stokuretim.repository.KullaniciRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Veritabanında emaile göre kullanıcı ara
        Kullanici kullanici = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        // 2. Rolünü Spring Security formatına çevir (ROLE_ADMIN gibi)
        String rolAdi = "ROLE_" + kullanici.getRol().getRolAdi();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rolAdi);

        // 3. Spring'in anlayacağı User nesnesini döndür
        return new User(
                kullanici.getEmail(),
                kullanici.getSifre(),
                Collections.singletonList(authority)
        );
    }
}