package com.AgusGalan.H2Commerce.controller;

import com.AgusGalan.H2Commerce.exceptions.BadRequestException;
import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import com.AgusGalan.H2Commerce.persistence.entities.ProductoDTO;
import com.AgusGalan.H2Commerce.service.CategoriaService;
import com.AgusGalan.H2Commerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/todos")
    public List<Producto> obtenerTodosLosProductos() throws ResourceNotFoundException{
        return productoService.mostrarTodosLosProductos();
    }

    @GetMapping("/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) throws ResourceNotFoundException{
        Producto producto = productoService.obtenerProductoPorId(id);
        return producto;
    }
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarProducto(@RequestBody Producto producto) throws BadRequestException {
        Producto productoGuardado = productoService.guardarProducto(producto);

        return ResponseEntity.ok("Producto agregado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable("id") Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarProducto(@PathVariable("id") Long id, @RequestBody Producto productoActualizado) {
        try {
            productoService.actualizarProducto(id, productoActualizado);
            return ResponseEntity.ok("Producto actualizado exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
