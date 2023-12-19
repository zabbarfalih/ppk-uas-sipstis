package com.zabbarfalih.sipstis.controller;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.service.PengajuanKepalaBauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kepala-bau/pengajuan")
public class KepalaBAUController {

    @Autowired
    private PengajuanKepalaBauService pengajuanKepalaBauService;

    @PutMapping("/{id}/setujui")
    public PengajuanDto setujuiPengajuan(@PathVariable Long id) {
        return pengajuanKepalaBauService.setujuiPengajuan(id);
    }

    @PutMapping("/{id}/tolak")
    public PengajuanDto tolakPengajuan(@PathVariable Long id) {
        return pengajuanKepalaBauService.tolakPengajuan(id);
    }

    @PutMapping("/{id}/revisi")
    public PengajuanDto revisiPengajuan(@PathVariable Long id) {
        return pengajuanKepalaBauService.revisiPengajuan(id);
    }
}
