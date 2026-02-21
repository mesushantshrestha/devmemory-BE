package com.shrestha.devmemory_BE.repository;

import com.shrestha.devmemory_BE.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByGoogleSub(String google_Sub);
}
