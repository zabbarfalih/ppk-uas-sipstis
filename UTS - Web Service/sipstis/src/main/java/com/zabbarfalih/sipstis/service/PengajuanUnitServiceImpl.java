package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.entity.Pengajuan;
import com.zabbarfalih.sipstis.entity.StatusEnum;
import com.zabbarfalih.sipstis.entity.User;
import com.zabbarfalih.sipstis.mapper.PengajuanMapper;
import com.zabbarfalih.sipstis.repository.PengajuanRepository;
import com.zabbarfalih.sipstis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PengajuanUnitServiceImpl implements PengajuanUnitService {

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PengajuanDto createPengajuan(PengajuanDto pengajuanDto) {
        if (pengajuanRepository.existsByDeskripsiPengadaan(pengajuanDto.getDeskripsiPengadaan())) {
            throw new RuntimeException("Error: Pengajuan dengan deskripsi barang '" + pengajuanDto.getDeskripsiPengadaan() + "' sudah ada!");
        }
        User user = userRepository.findById(pengajuanDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User dengan id '" + pengajuanDto.getUserId() + "' tidak ditemukan"));
        Pengajuan pengajuan = PengajuanMapper.mapToPengajuan(pengajuanDto, user);
        pengajuan.setIsPengajuanPBJ(false);
        pengajuan.setIsPengajuanPPK(false);
        pengajuan.setStatus(StatusEnum.MENUNGGU_PERSETUJUAN);
        pengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }

    @Override
    public PengajuanDto getPengajuanById(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).orElse(null);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }


    @Override
    public List<PengajuanDto> getAllPengajuanByUserId(Long userId) {
        List<Pengajuan> pengajuans = pengajuanRepository.findAllByUserId(userId);
        return pengajuans.stream()
                .map(PengajuanMapper::mapToPengajuanDto)
                .collect(Collectors.toList());
    }
    @Override
    public PengajuanDto updatePengajuan(Long id, PengajuanDto pengajuanDto, Long userId) {
        Pengajuan existingPengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pengajuan dengan ID '" + id + "' tidak ada!"));

        if (existingPengajuan.getStatus() != StatusEnum.REVISI) {
            throw new RuntimeException("Pengajuan dengan status '" + existingPengajuan.getStatus() + "' tidak dapat disetujui.");
        }

        if (!existingPengajuan.getUser().getId().equals(userId)) {
            throw new RuntimeException("Pengajuan tidak dimiliki oleh user dengan ID '" + userId + "' yaitu '" + existingPengajuan.getNamaPengadaan() + "'");
        }

        if (existingPengajuan.getStatus() != StatusEnum.REVISI) {
            throw new RuntimeException("Pengajuan dengan status '" + existingPengajuan.getStatus() + "' tidak dapat diedit!");
        }

        existingPengajuan.setDeskripsiPengadaan(pengajuanDto.getDeskripsiPengadaan());
        existingPengajuan.setJumlah(pengajuanDto.getJumlah());
        existingPengajuan.setEstimasiHargaSatuan(pengajuanDto.getEstimasiHargaSatuan());
        existingPengajuan.setEstimasiHargaTotal(pengajuanDto.getEstimasiHargaTotal());
        existingPengajuan.setStatus(StatusEnum.MENUNGGU_PERSETUJUAN);

        Pengajuan updatedPengajuan = pengajuanRepository.save(existingPengajuan);

        return PengajuanMapper.mapToPengajuanDto(updatedPengajuan);
    }

    @Override
    public void deletePengajuan(Long id) {
        Pengajuan existingPengajuan = pengajuanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pengajuan dengan ID '" + id + "' tidak ditemukan!"));

        if (existingPengajuan.getStatus() != StatusEnum.MENUNGGU_PERSETUJUAN && existingPengajuan.getStatus() != StatusEnum.DITOLAK) {
            throw new RuntimeException("Hanya pengajuan dengan status MENUNGGU_PERSETUJUAN atau DITOLAK yang dapat dihapus!");
        }

        pengajuanRepository.deleteById(id);
    }
}