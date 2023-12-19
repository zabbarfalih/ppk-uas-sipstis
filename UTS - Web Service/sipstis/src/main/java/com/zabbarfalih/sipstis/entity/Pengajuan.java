package com.zabbarfalih.sipstis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pengajuan")
public class Pengajuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pengajuan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String namaPengadaan;

    @Column(nullable = false)
    private LocalDate tanggalPengadaan;

    @Column(nullable = false)
    private String deskripsiPengadaan;

    @Column(nullable = false)
    private Integer jumlah;

    @Column(nullable = false)
    private Double estimasiHargaSatuan;

    @Column(nullable = false)
    private Double estimasiHargaTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @Column(nullable = false)
    private Boolean isPengajuanPPK = false;

    @Column(nullable = false)
    private Boolean isPengajuanPBJ = false;

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        if (estimasiHargaSatuan != null && jumlah != null) {
            this.estimasiHargaTotal = estimasiHargaSatuan * jumlah;
        } else {
            this.estimasiHargaTotal = 0.0;
        }
    }
}
