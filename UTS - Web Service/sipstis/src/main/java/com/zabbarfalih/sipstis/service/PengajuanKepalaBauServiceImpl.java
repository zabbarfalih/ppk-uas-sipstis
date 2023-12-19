package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.entity.Pengajuan;
import com.zabbarfalih.sipstis.entity.StatusEnum;
import com.zabbarfalih.sipstis.mapper.PengajuanMapper;
import com.zabbarfalih.sipstis.repository.PengajuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PengajuanKepalaBauServiceImpl implements PengajuanKepalaBauService {
    @Autowired
    private PengajuanRepository pengajuanRepository;


    @Override
    public PengajuanDto setujuiPengajuan(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).get();

        if (pengajuan.getStatus() != StatusEnum.MENUNGGU_PERSETUJUAN) {
            throw new RuntimeException("Pengajuan dengan status '" + pengajuan.getStatus() + "' tidak dapat disetujui.");
        }

        Double estimasiHargaTotal = pengajuan.getEstimasiHargaTotal();

        if (estimasiHargaTotal <= 200_000_000) {
            pengajuan.setStatus(StatusEnum.DIPROSES_PBJ);
            pengajuan.setIsPengajuanPBJ(true);
            pengajuan.setIsPengajuanPPK(false);
        } else {
            pengajuan.setStatus(StatusEnum.DIPROSES_PPK);
            pengajuan.setIsPengajuanPPK(true);
            pengajuan.setIsPengajuanPBJ(false);
        }

        pengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }

    @Override
    public PengajuanDto tolakPengajuan(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).get();

        if (pengajuan.getStatus() != StatusEnum.MENUNGGU_PERSETUJUAN) {
            throw new RuntimeException("Pengajuan dengan status '" + pengajuan.getStatus() + "' tidak dapat disetujui.");
        }

        pengajuan.setStatus(StatusEnum.DITOLAK);
        pengajuan = pengajuanRepository.save(pengajuan);

        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }

    @Override
    public PengajuanDto revisiPengajuan(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).get();

        if (pengajuan.getStatus() != StatusEnum.MENUNGGU_PERSETUJUAN) {
            throw new RuntimeException("Pengajuan dengan status '" + pengajuan.getStatus() + "' tidak dapat disetujui.");
        }

        pengajuan.setStatus(StatusEnum.REVISI);
        pengajuan = pengajuanRepository.save(pengajuan);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }
}
