package com.zabbarfalih.sipstis.auth;

import com.zabbarfalih.sipstis.dto.UserDto;
import com.zabbarfalih.sipstis.entity.User;
import com.zabbarfalih.sipstis.repository.UserRepository;
import com.zabbarfalih.sipstis.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getAccessToken(request);
        if (!jwtUtils.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDto userDetails = userDetailsService.loadUserByUsername(jwtUtils.getEmailFromToken(token));
        UsernamePasswordAuthenticationToken authentication = new
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication authenticationNow = SecurityContextHolder.getContext().getAuthentication();

        if (authenticationNow != null) {
            Object principal = authenticationNow.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetailsNow = (UserDetails) principal;
                String emailNow = userDetailsNow.getUsername();
            }
        }
    }
    private UserDto getUserDetails(String token) {
        String subject = jwtUtils.getSubject(token);
        User user = userRepository.findByEmail(subject);
        return UserDto.build(user);
    }
}
