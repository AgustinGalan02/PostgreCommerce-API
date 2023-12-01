package com.AgusGalan.H2Commerce.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

    @GetMapping("/accesoAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin() {
        return "Hola, has accedido con rol de ADMIN";
    }

    @GetMapping("/accesoUser")
    @PreAuthorize("hasRole('USER')")
    public String acessUser() {
        return "Hola, has accedido con rol de USUARIO";
    }

}
