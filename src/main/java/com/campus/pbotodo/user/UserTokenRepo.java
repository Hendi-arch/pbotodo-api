package com.campus.pbotodo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserTokenRepo extends JpaRepository<UserToken, Long> {

    UserToken findByUsernameAndToken(String username, String token);

    UserToken findByToken(String token);

    @Query("SELECT u FROM UserToken u WHERE u.username = :username AND u.isActive = true AND u.expiredDate >= now() ORDER BY u.createdAt DESC")
    List<UserToken> findActiveDeviceIds(String username);

}
