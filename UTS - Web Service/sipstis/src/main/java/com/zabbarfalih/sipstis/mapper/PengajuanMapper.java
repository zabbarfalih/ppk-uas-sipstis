package com.zabbarfalih.sipstis.mapper;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.entity.Pengajuan;
import com.zabbarfalih.sipstis.entity.User;

public class PengajuanMapper {
    public static Pengajuan mapToPengajuan(PengajuanDto pengajuanDto, User user) {
        return Pengajuan.builder()
                .id(pengajuanDto.getId())
                .user(user)
                .namaPengadaan(pengajuanDto.getNamaPengadaan())
                .tanggalPengadaan(pengajuanDto.getTanggalPengadaan())
                .deskripsiPengadaan(pengajuanDto.getDeskripsiPengadaan())
                .jumlah(pengajuanDto.getJumlah())
                .estimasiHargaSatuan(pengajuanDto.getEstimasiHargaSatuan())
                .estimasiHargaTotal(pengajuanDto.getEstimasiHargaTotal())
                .status(pengajuanDto.getStatus())
                .isPengajuanPPK(pengajuanDto.getIsPengajuanPPK())
                .isPengajuanPBJ(pengajuanDto.getIsPengajuanPBJ())
                .build();
    }

    public static PengajuanDto mapToPengajuanDto(Pengajuan pengajuan) {
        return PengajuanDto.builder()
                .id(pengajuan.getId())
                .userId(pengajuan.getUser().getId())
                .namaPengadaan(pengajuan.getNamaPengadaan())
                .tanggalPengadaan(pengajuan.getTanggalPengadaan())
                .deskripsiPengadaan(pengajuan.getDeskripsiPengadaan())
                .jumlah(pengajuan.getJumlah())
                .estimasiHargaSatuan(pengajuan.getEstimasiHargaSatuan())
                .estimasiHargaTotal(pengajuan.getEstimasiHargaTotal())
                .status(pengajuan.getStatus())
                .isPengajuanPPK(pengajuan.getIsPengajuanPPK())
                .isPengajuanPBJ(pengajuan.getIsPengajuanPBJ())
                .build();
    }
}
