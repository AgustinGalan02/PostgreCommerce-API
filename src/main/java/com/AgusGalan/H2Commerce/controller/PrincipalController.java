package com.AgusGalan.H2Commerce.controller;

import com.AgusGalan.H2Commerce.persistence.entities.CreateUserDTO;
import com.AgusGalan.H2Commerce.persistence.entities.ERole;
import com.AgusGalan.H2Commerce.persistence.entities.RoleEntity;
import com.AgusGalan.H2Commerce.persistence.entities.UserEntity;
import com.AgusGalan.H2Commerce.persistence.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class PrincipalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World Not Secured";
    }

    @GetMapping("/helloSecured")
    public String helloSecured() {
        return "Hello World Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) { // espera un objeto JSON en ele cuerpo de la solicitud

        // SE MAPEA UN CONJUNTO DE ROLES DEL DTO a un conjunto de entidades de roles (RoleEntity)
        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        // SE CREA UNA NUEVA INSTANCIA DE USERENTITY, SE ASIGNAN LOS VALORES DEL DTO A LOS CAMPOS DEL OBJETO USERENTITY.
        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el user con id: ".concat(id);
    }

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }
}
