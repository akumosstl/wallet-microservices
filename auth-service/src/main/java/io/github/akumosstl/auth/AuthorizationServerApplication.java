package io.github.akumosstl.auth;

import io.github.akumosstl.auth.model.ERole;
import io.github.akumosstl.auth.model.Role;
import io.github.akumosstl.auth.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.Optional;


@SpringBootApplication
@EnableEurekaClient
public class AuthorizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner addRoles(RoleRepository repo) {
        return args -> {
            Optional<Role> adminPersisted = repo.findByName(ERole.ROLE_ADMIN);
            if (adminPersisted.isEmpty()){
                var admin = new Role();
                ERole erole = ERole.ROLE_ADMIN;
                admin.setName(erole);

                repo.save(admin);

            }
            Optional<Role> userPersisted = repo.findByName(ERole.ROLE_USER);
            if (userPersisted.isEmpty()){
                var user = new Role();
                ERole erole = ERole.ROLE_USER;
                user.setName(erole);

                repo.save(user);

            }

        };
    }
}
