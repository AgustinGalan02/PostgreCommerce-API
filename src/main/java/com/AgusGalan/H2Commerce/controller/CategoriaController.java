package com.AgusGalan.H2Commerce.controller;

import com.AgusGalan.H2Commerce.exceptions.BadRequestException;
import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.entities.Pedido;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import com.AgusGalan.H2Commerce.service.CategoriaService;
import com.AgusGalan.H2Commerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/todos")
    public List<Categoria> obtenerTodasLasCategorias() throws ResourceNotFoundException{
        return categoriaService.mostrarTodasLasCategorias();
    }

    @GetMapping("/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) throws ResourceNotFoundException {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
        return categoria;

    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarCategoria(@RequestBody Categoria categoria) throws BadRequestException {
        Categoria categoriaGuardada = categoriaService.guardarCategoria(categoria);

        return ResponseEntity.ok("Categoria agregada correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable("id") Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.ok("Categoria eliminada exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCategoria(@PathVariable("id") Long id, @RequestBody Categoria categoriaActualizada) {
        try {
            categoriaService.actualizarCategoria(id, categoriaActualizada);
            return ResponseEntity.ok("Categoria actualizada exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

