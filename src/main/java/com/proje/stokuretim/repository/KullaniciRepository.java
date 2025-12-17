package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.Kullanici; // Senin sınıf ismin Kullanicilar ise
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Integer> {
    // Giriş işlemi için emaile göre kullanıcı bulmamız gerekecek
    Optional<Kullanici> findByEmail(String email);
}