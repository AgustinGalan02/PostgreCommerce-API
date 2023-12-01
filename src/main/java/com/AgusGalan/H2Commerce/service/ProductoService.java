package com.AgusGalan.H2Commerce.service;

import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import com.AgusGalan.H2Commerce.persistence.repository.CategoriaRepository;
import com.AgusGalan.H2Commerce.persistence.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.AgusGalan.H2Commerce.exceptions.BadRequestException;
import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> mostrarTodosLosProductos() throws ResourceNotFoundException {
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("Actualmente no existe ningun producto en el sistema");
        }
        return productos;
    }

    public Producto obtenerProductoPorId(Long id) throws ResourceNotFoundException {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()) {
            return productoOptional.orElse(null);
        } else {
            throw new ResourceNotFoundException("No se encontro el producto con el ID: " + id);
        }
    }

    public Producto guardarProducto(Producto producto) throws BadRequestException {

        if (producto.getNombre().isEmpty() || producto.getCategoria() == null || producto.getDescripcion().isEmpty() || producto.getPrecio() == 0 || Double.isNaN(producto.getStock())) {
        throw new BadRequestException("Ningun campo puede estar vacio");
    }
        Categoria categoria = producto.getCategoria();
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoria.getId());
        if (categoriaOptional.isEmpty()) {
            throw new BadRequestException("La categoría ingresada no existe");
        }
            return productoRepository.save(producto);
}

    public void eliminarProducto(Long id) throws ResourceNotFoundException{
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();

            producto.getPedidos().forEach(pedido -> pedido.getProductos().remove(producto));

            productoRepository.delete(producto);
        } else {
            throw new ResourceNotFoundException("No existe ningun producto con el ID: " + id);
        }
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) throws BadRequestException, ResourceNotFoundException {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();

            if (productoActualizado.getNombre().isEmpty() ||
                    productoActualizado.getDescripcion().isEmpty() ||
                    productoActualizado.getPrecio() == 0 ||
                    Double.isNaN(productoActualizado.getStock()) || productoActualizado.getStock() == 0) {
                throw new BadRequestException("Los campos no pueden estar vacíos");
            }

            Categoria categoriaActualizada = productoActualizado.getCategoria();
            Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoriaActualizada.getId());
            if (categoriaOptional.isEmpty()) {
                throw new BadRequestException("La categoría ingresada no existe");
            }

            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            producto.setCategoria(productoActualizado.getCategoria());

            return productoRepository.save(producto);
        } else {
            throw new ResourceNotFoundException("No se encontró un producto con el ID: " + id);
        }
    }

    public boolean existeProducto(Long productoId) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        return productoOptional.isPresent();
    }

}