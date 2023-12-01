package com.AgusGalan.H2Commerce.persistence.repository;

import com.AgusGalan.H2Commerce.persistence.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
