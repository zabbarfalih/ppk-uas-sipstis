package com.zabbarfalih.sipstis.dto;

import com.zabbarfalih.sipstis.entity.StatusEnum;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PengajuanDto {
    private Long id;
    private Long userId;
    private String namaPengadaan;
    private LocalDate tanggalPengadaan;
    private String deskripsiPengadaan;
    private Integer jumlah;
    private Double estimasiHargaSatuan;
    private Double estimasiHargaTotal;
    private StatusEnum status;
    private Boolean isPengajuanPPK;
    private Boolean isPengajuanPBJ;

}
