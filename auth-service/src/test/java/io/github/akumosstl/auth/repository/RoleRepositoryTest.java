package io.github.akumosstl.auth.repository;

import io.github.akumosstl.auth.model.ERole;
import io.github.akumosstl.auth.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should find role by role name")
    void shouldFindRoleByName() {
        ERole roleUser = ERole.ROLE_USER;

        Optional<Role> found = roleRepository.findByName(roleUser);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(ERole.ROLE_USER);
    }

}
