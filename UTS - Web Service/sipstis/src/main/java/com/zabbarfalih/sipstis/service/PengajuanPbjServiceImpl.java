package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.entity.StatusEnum;
import com.zabbarfalih.sipstis.mapper.PengajuanMapper;
import com.zabbarfalih.sipstis.repository.PengajuanRepository;
import com.zabbarfalih.sipstis.entity.Pengajuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PengajuanPbjServiceImpl implements PengajuanPbjService {

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Override
    public PengajuanDto pembelianPengajuanPbj(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).orElseThrow(() -> new RuntimeException("Pengajuan tidak ditemukan"));

        pengajuan.setStatus(StatusEnum.DIPROSES_PBJ_PEMBELIAN);
        pengajuanRepository.save(pengajuan);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }

    @Override
    public PengajuanDto selesaikanPengajuanPbj(Long id) {
        Pengajuan pengajuan = pengajuanRepository.findById(id).orElseThrow(() -> new RuntimeException("Pengajuan tidak ditemukan"));

        if(pengajuan.getStatus() != StatusEnum.DIPROSES_PBJ_PEMBELIAN) {
            throw new RuntimeException("Pengajuan tidak dalam status yang valid untuk proses penyelesaian");
        }
        pengajuan.setStatus(StatusEnum.SELESAI);
        pengajuanRepository.save(pengajuan);
        return PengajuanMapper.mapToPengajuanDto(pengajuan);
    }
}
