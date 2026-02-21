package com.shrestha.devmemory_BE.dto;

import java.util.UUID;

public record AuthMeResponse(
        boolean loggedIn,
        UUID userId,
        String email,
        String name,
        String pictureUrl
) {
    public static AuthMeResponse loogedOut(){
        return new AuthMeResponse(false, null, null, null, null);
    }
}
