package com.AgusGalan.H2Commerce.controller;

import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.Pedido;
import com.AgusGalan.H2Commerce.persistence.entities.PedidoDTO;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import com.AgusGalan.H2Commerce.persistence.repository.PedidoRepository;
import com.AgusGalan.H2Commerce.persistence.repository.ProductoRepository;
import com.AgusGalan.H2Commerce.service.PedidoService;
import com.AgusGalan.H2Commerce.service.ProductoService;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPedido(@RequestBody Pedido pedido) throws ResourceNotFoundException {

        if (pedido.getEstado_pedido() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta agregar el estado del pedido");
        }

        if (pedido.getProductos() == null || pedido.getProductos().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta agregar producto/s");
        }

        for (Producto producto : pedido.getProductos()) {
            if (!productoService.existeProducto(producto.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingresó un producto no existente");
            }
        }

        try {
            pedidoService.guardarPedido(pedido);
            return ResponseEntity.ok("Pedido agregado exitosamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El pedido no se pudo agregar, revisar");
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Pedido>> mostrartodoslospedidos() {
        List<Pedido> pedidos = pedidoService.mostrartodoslospedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public Pedido obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);

        if (pedido == null) {
            throw new NoSuchElementException("Pedido no encontrado");
        }
        return pedido;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPedido(@PathVariable("id") Long id) {
        try {
            pedidoService.eliminarPedido(id);
            return ResponseEntity.ok("Pedido eliminado exitosamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el pedido");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPedido(@PathVariable("id") Long id, @RequestBody Pedido pedidoActualizado) {

        if (pedidoActualizado.getEstado_pedido() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta agregar el estado del pedido");
        }

        if (pedidoActualizado.getProductos() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta agregar producto/s");
        }

        for (Producto producto : pedidoActualizado.getProductos()) {
            if (!productoService.existeProducto(producto.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingresó un producto no existente");
            }
        }

        try {
            pedidoService.actualizarPedido(id, pedidoActualizado);
            return ResponseEntity.ok("Pedido actualizado exitosamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }
    }
}