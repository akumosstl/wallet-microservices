package io.github.akumosstl.auth.repository;

import io.github.akumosstl.auth.model.ERole;
import io.github.akumosstl.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
