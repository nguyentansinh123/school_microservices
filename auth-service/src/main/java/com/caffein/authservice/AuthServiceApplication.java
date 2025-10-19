package com.caffein.authservice;

import com.caffein.authservice.model.Permission;
import com.caffein.authservice.model.Role;
import com.caffein.authservice.model.User;
import com.caffein.authservice.repository.PermissionRepository;
import com.caffein.authservice.repository.RoleRepository;
import com.caffein.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository,PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Permissions with a resource:action convention
            Permission userRead = permissionRepository.findByName("user:read").orElseGet(() -> permissionRepository.save(Permission.builder().name("user:read").build()));
            Permission userWrite = permissionRepository.findByName("user:write").orElseGet(() -> permissionRepository.save(Permission.builder().name("user:write").build()));
            Permission userDelete = permissionRepository.findByName("user:delete").orElseGet(() -> permissionRepository.save(Permission.builder().name("user:delete").build()));
            Permission adminRead = permissionRepository.findByName("admin:read").orElseGet(() -> permissionRepository.save(Permission.builder().name("admin:read").build()));
            Permission adminWrite = permissionRepository.findByName("admin:write").orElseGet(() -> permissionRepository.save(Permission.builder().name("admin:write").build()));
            Permission gradesView = permissionRepository.findByName("grades:view").orElseGet(() -> permissionRepository.save(Permission.builder().name("grades:view").build()));

            // Create Roles and assign permissions
            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
            if (CollectionUtils.isEmpty(userRole.getPermissions())) {
                userRole.setPermissions(List.of(userRead, userWrite));
                roleRepository.save(userRole);
            }

            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));
            if (CollectionUtils.isEmpty(adminRole.getPermissions())) {
                adminRole.setPermissions(List.of(adminRead, adminWrite, userDelete, gradesView));
                roleRepository.save(adminRole);
            }

            Role studentRole = roleRepository.findByName("ROLE_STUDENT").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_STUDENT").build()));
            if (CollectionUtils.isEmpty(studentRole.getPermissions())) {
                List<Permission> studentPermissions = new ArrayList<>(List.of(userRead, userWrite, gradesView));
                studentRole.setPermissions(studentPermissions);
                roleRepository.save(studentRole);
            }
            if (userRepository.findByEmailIgnoreCase("admin@mail.com").isEmpty()) {
                User adminUser = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin@mail.com")
                        .password(passwordEncoder.encode("password")) // Default password
                        .phoneNumber("+10000000000") // Dummy phone number
                        .roles(List.of(adminRole))
                        .enabled(true)
                        .emailVerified(true)
                        .build();
                userRepository.save(adminUser);
            }
            
        };
    }
}
