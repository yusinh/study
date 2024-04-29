package com.mysite.sbb.user.repository;


import java.util.Optional;

import com.mysite.sbb.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String Username);

    Optional<SiteUser> findByusername(String username);
}
