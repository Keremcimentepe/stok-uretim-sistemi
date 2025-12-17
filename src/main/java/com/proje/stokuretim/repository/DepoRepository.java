package com.proje.stokuretim.repository;

import com.proje.stokuretim.entity.Depo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepoRepository extends JpaRepository<Depo, Integer> {
}