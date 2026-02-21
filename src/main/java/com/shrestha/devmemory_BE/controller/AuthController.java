package com.shrestha.devmemory_BE.controller;

import com.shrestha.devmemory_BE.dto.AuthMeResponse;
import com.shrestha.devmemory_BE.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedIn(){

        return userRepository.findById(currentUserId)
                .map(u -> ResponseEntity.ok(new AuthMeResponse(
                        true,
                        u.getId(),
                        u.getEmail(),
                        u.getName(),
                        u.getPicture_url()
                )))
                .orElseGet(() -> ResponseEntity.ok(AuthMeResponse.loogedOut()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        request.getSession(false);
        var session = request.getSession(false);
        if(session !=null) session.invalidate();

        return ResponseEntity.noContent().build();
    }

}
