package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.Rol; // Sınıf ismin Roller ise
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // Admin mi User mı olduğunu adından bulmak için
    Optional<Rol> findByRolAdi(String rolAdi);
}