package com.zabbarfalih.sipstis.repository;

import com.zabbarfalih.sipstis.entity.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, Long> {
    Optional<Pengajuan> findByIdAndUserId(Long id, Long userId);
    List<Pengajuan> findAllByUserId(Long userId);
    boolean existsByDeskripsiPengadaan(String deskripsiPengadaan);
}
