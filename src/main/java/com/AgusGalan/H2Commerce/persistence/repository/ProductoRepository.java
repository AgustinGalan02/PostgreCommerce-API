package com.AgusGalan.H2Commerce.persistence.repository;

import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(Categoria categoria);

}
