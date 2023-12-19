package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;

import java.util.List;

public interface PengajuanUnitService {

    PengajuanDto createPengajuan(PengajuanDto pengajuanDto);

    PengajuanDto getPengajuanById(Long id);


    List<PengajuanDto> getAllPengajuanByUserId(Long userId);

    PengajuanDto updatePengajuan(Long id, PengajuanDto pengajuanDto, Long userId);

    void deletePengajuan(Long id);
}
