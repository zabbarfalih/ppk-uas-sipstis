package com.zabbarfalih.sipstis.controller;


import com.zabbarfalih.sipstis.dto.ProfileChangePasswordRequest;
import com.zabbarfalih.sipstis.dto.ProfileEditRequest;
import com.zabbarfalih.sipstis.dto.ProfileResponse;
import com.zabbarfalih.sipstis.entity.User;
import com.zabbarfalih.sipstis.exception.UnauthorizedException;
import com.zabbarfalih.sipstis.mapper.UserMapper;
import com.zabbarfalih.sipstis.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class UserProfileController {
    final
    UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getProfile() {
        try {
            User user = userService.getUserLogged();
            ProfileResponse profileResponse = UserMapper.toProfileResponse(user);
            return ResponseEntity.ok(profileResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping
    public ResponseEntity<Object> deleteProfile(Authentication authentication,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            userService.deleteCurrentUser();
            new SecurityContextLogoutHandler().logout(request,
                    response,
                    authentication);
            return ResponseEntity.ok("Account deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> editProfile(@Valid @RequestBody ProfileEditRequest request) {
        try {
            ProfileResponse user = userService.changeProfile(request.getName(), request.getNip(), request.getEmail(), request.getNoTelepon());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ProfileChangePasswordRequest request) {
        try {
            ProfileResponse user = userService.changePassword(request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
