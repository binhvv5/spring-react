package com.ip.springdemo.repository;

import com.ip.springdemo.domain.Authority;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByName(String name);
    Optional<Authority> findOneByName(String name);
}
