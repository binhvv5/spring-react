package com.ip.springdemo.repository;

import com.ip.springdemo.domain.Authority;
import com.ip.springdemo.domain.UserAuthority;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    List<UserAuthority> findByAuthorityName(String name);
}
