package com.AgusGalan.H2Commerce.persistence.repository;

import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
