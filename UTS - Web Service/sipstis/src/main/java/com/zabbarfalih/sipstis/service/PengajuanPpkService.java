package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;

public interface PengajuanPpkService {

    PengajuanDto selesaikanPengajuanPpk(Long id);
    PengajuanDto pembelianPengajuanPpk(Long id);
}
