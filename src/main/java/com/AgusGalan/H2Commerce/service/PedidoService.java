package com.AgusGalan.H2Commerce.service;

import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.*;
import com.AgusGalan.H2Commerce.persistence.repository.PedidoRepository;
import com.AgusGalan.H2Commerce.persistence.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ProductoService productoService;

    public List<Pedido> mostrartodoslospedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            pedido.getProductos().size(); // se accede a la lista de productos para cargarla
        }
        return pedidos;
    }

    public Pedido obtenerPedidoPorId(Long id) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        return pedidoOptional.orElse(null);
    }

    public void guardarPedido(Pedido pedido) throws ResourceNotFoundException {
        // se crea un HashSet para almacenar los productos actualiados
        Set<Producto> productos = new HashSet<>();

        // se recorre la lista de productos en el pedido seleccionado
        for (Producto producto : pedido.getProductos()) {
            // obtenemos los productos existentes
            Producto productoExistente = productoService.obtenerProductoPorId(producto.getId());
            // añadimos los productos al pedido
            productos.add(productoExistente);
        }
        // se actualizan los productos en el pedido
        pedido.setProductos(productos);
        pedidoRepository.save(pedido);
    }

    public void eliminarPedido(Long id) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedidoRepository.delete(pedido);
    }

    public void actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());

        pedido.setEstado_pedido(pedidoActualizado.getEstado_pedido());
        pedido.setProductos(pedidoActualizado.getProductos());

        pedidoRepository.save(pedido);
    }

}

