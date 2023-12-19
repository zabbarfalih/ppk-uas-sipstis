package com.zabbarfalih.sipstis.controller;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.service.PengajuanPbjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pbj/pengajuan")
public class PBJController {
    @Autowired
    private PengajuanPbjService pengajuanPbjService;


    @PutMapping("/{id}/pembelian")
    public PengajuanDto pembelianPengajuanPbj(@PathVariable Long id) {
        return pengajuanPbjService.pembelianPengajuanPbj(id);
    }

    @PutMapping("/{id}/selesai")
    public PengajuanDto selesaikanPengajuanPbj(@PathVariable Long id) {
        return pengajuanPbjService.selesaikanPengajuanPbj(id);
    }
}