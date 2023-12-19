package com.zabbarfalih.sipstis.controller;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.service.PengajuanPpkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ppk/pengajuan")
public class PPKController {
    @Autowired
    private PengajuanPpkService pengajuanPpkService;

    @PutMapping("/{id}/pembelian")
    public PengajuanDto pembelianPengajuanPpk(@PathVariable Long id) {
        return pengajuanPpkService.pembelianPengajuanPpk(id);
    }

    @PutMapping("/{id}/selesai")
    public PengajuanDto selesaikanPengajuanPpk(@PathVariable Long id) {
        return pengajuanPpkService.selesaikanPengajuanPpk(id);
    }
}