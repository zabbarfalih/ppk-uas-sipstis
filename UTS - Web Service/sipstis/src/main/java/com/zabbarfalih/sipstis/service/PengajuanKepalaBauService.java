package com.zabbarfalih.sipstis.service;

import com.zabbarfalih.sipstis.dto.PengajuanDto;

public interface PengajuanKepalaBauService {

    PengajuanDto setujuiPengajuan(Long id);

    PengajuanDto tolakPengajuan(Long id);

    PengajuanDto revisiPengajuan(Long id);
}
