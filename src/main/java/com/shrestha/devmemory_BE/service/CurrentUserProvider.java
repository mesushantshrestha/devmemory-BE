package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserProvider {
    private final UserService userService;

    public CurrentUserProvider(UserService userService) {
        this.userService = userService;
    }

    public UUID getCurrentUserID(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()) throw new IllegalStateException("Not authenticated");

        Object principalObj = auth.getPrincipal();
        if(!(principalObj instanceof OAuth2User principal)) throw new IllegalStateException("Unsupported principal type: "+ principalObj);

        Users user = userService.upsertFromGoogle(principal);
        return user.getId();
    }

}
