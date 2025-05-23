package com.chatapp.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapp.api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.username = :username AND SET u.description =:description WHERE u.id = :id")
    void patchProfile(@Param("id") Long id,@Param("username") String username, @Param("description") String description);
    

    @Modifying
    @Query("UPDATE User u SET u.refreshToken = :refreshToken WHERE u.id = :id")
    void updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("id") Long id);
}
