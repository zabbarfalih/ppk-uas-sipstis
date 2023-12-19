package com.zabbarfalih.sipstis.controller;

import com.zabbarfalih.sipstis.dto.PengajuanDto;
import com.zabbarfalih.sipstis.entity.User;
import com.zabbarfalih.sipstis.repository.UserRepository;
import com.zabbarfalih.sipstis.service.UserService;
import com.zabbarfalih.sipstis.service.PengajuanUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/unit/pengajuan")
public class UnitController {

    @Autowired
    private PengajuanUnitService pengajuanService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public PengajuanDto createPengajuan(@RequestBody PengajuanDto pengajuanDto) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User tidak ditemukan dengan id '" + userId + "'"
        ));
        pengajuanDto.setUserId(userId);
        return pengajuanService.createPengajuan(pengajuanDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PengajuanDto> updatePengajuan(@PathVariable Long id, @RequestBody PengajuanDto pengajuanDto) {
        Long userId = getCurrentUserId();
        PengajuanDto updatedPengajuan = pengajuanService.updatePengajuan(id, pengajuanDto, userId);
        if (updatedPengajuan != null) {
            return new ResponseEntity<>(updatedPengajuan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public PengajuanDto getPengajuanById(@PathVariable Long id) {
        return pengajuanService.getPengajuanById(id);
    }

    @GetMapping
    public List<PengajuanDto> getAllPengajuan() {
        return pengajuanService.getAllPengajuanByUserId(getCurrentUserId());
    }

    @DeleteMapping("/{id}")
    public void deletePengajuan(@PathVariable Long id) {
        pengajuanService.deletePengajuan(id);
    }


    // User yang sedang login
    private Long getCurrentUserId() {
        Authentication authenticationNow = SecurityContextHolder.getContext().getAuthentication();

        if (authenticationNow != null) {
            Object principal = authenticationNow.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetailsNow = (UserDetails) principal;
                User user = userRepository.findByEmail(userDetailsNow.getUsername());

                if (user != null) {
                    return user.getId();
                }
            }
        }

        throw new RuntimeException("User belum diautentikasi");
    }
}
