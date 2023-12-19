package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;

public interface PengajuanPbjService {

    PengajuanDto selesaikanPengajuanPbj(Long id);
    PengajuanDto pembelianPengajuanPbj(Long id);

}
