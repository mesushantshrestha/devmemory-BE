package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.entity.Users;
import com.shrestha.devmemory_BE.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Users upsertFromGoogle(OAuth2User principal){
        String sub = principal.getAttribute("sub");
        String email= principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");

        if(sub == null){
            throw new IllegalStateException("Google principal missing 'sub' claim");
        }
        if(email == null){
            email="unknown";
        }

        Users users = userRepository.findByGoogleSub(sub).orElseGet(() ->{
           Users u = new Users();
           u.setId(UUID.randomUUID());
           u.setGoogleSub(sub);
           return u;
        });

        users.setEmail(email);
        users.setName(name);
        users.setPicture_url(picture);

        return userRepository.save(users);
    }

    @Transactional
    public Users getByGoogleSubOrThrow(String google_Sub){
        return userRepository.findByGoogleSub(google_Sub)
                .orElseThrow(() -> new IllegalStateException("User not found for google_sub="+ google_Sub));
    }
}
