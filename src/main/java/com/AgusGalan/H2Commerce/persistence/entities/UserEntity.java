package com.AgusGalan.H2Commerce.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data // GENERA AUTOMATICAMENTE LOS GETTERS Y SETTERS
@AllArgsConstructor // GENERA UN CONSTRUCTOR QUE TOMA COMO PARAMETRO TODOS LOS CAMPOS DE LA CLASE
@NoArgsConstructor // GENERA UN CONSTRUCTOR SIN ARGUMENTOS
@Builder // GENERA UN PATRON DE DISEÑO BUILDER QUE AHORRA CODIGO
@Entity // MARCA LA CLASE COMO UNA TABLA EN LA BASE DE DATOS
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank // NO PUEDE SER NULO
    @Size(max = 80)
    private String email;

    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;



}
