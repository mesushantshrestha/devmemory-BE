package com.shrestha.devmemory_BE.controller;

import com.shrestha.devmemory_BE.dto.AuthMeResponse;
import com.shrestha.devmemory_BE.entity.Users;
import com.shrestha.devmemory_BE.repository.UserRepository;
import com.shrestha.devmemory_BE.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Value("${app.dev.single-user-id}")
    private UUID currentUserId;
    private final UserRepository userRepository;
    private final UserService userService;
    public AuthController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedIn(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null){
            return ResponseEntity.ok(AuthMeResponse.loogedOut());
        }

        Object principalObj = auth.getPrincipal();
        if(!(principalObj instanceof OAuth2User principal)){
            return ResponseEntity.ok(AuthMeResponse.loogedOut());
        }

        Users user = userService.upsertFromGoogle(principal);

        return ResponseEntity.ok(new AuthMeResponse(
                true,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPicture_url()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        var session = request.getSession(false);
        if(session !=null) session.invalidate();

        return ResponseEntity.noContent().build();
    }

}
