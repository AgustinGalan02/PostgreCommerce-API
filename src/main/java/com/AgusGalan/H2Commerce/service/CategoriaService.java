package com.AgusGalan.H2Commerce.service;

import com.AgusGalan.H2Commerce.exceptions.BadRequestException;
import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.entities.Producto;
import com.AgusGalan.H2Commerce.persistence.repository.CategoriaRepository;
import com.AgusGalan.H2Commerce.persistence.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Categoria> mostrarTodasLasCategorias() throws ResourceNotFoundException {
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            throw new ResourceNotFoundException("Actualmente no existe ninguna categoría en el sistema");
        }
        return categorias;
    }

    public Categoria obtenerCategoriaPorId(Long id) throws ResourceNotFoundException{
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);

        if(categoriaOptional.isPresent()){
            return categoriaOptional.orElse(null);
        } else {
            throw new ResourceNotFoundException("No se encontró la categoria con el ID: " + id);
        }
    }

    public Categoria guardarCategoria(Categoria categoria) throws BadRequestException {
        if (categoria.getNombre().isEmpty() || categoria.getDescripcion().isEmpty()) {
            throw new BadRequestException("El nombre y/o la descripción estan vacios");
        }

        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) throws BadRequestException, ResourceNotFoundException {
        Categoria categoria = obtenerCategoriaPorId(id);
        if (categoria == null) {
            throw new ResourceNotFoundException("No se encontró la categoría con el ID: " + id);
        }
        if (categoriaActualizada.getNombre().isEmpty() || categoriaActualizada.getDescripcion().isEmpty()) {
            throw new BadRequestException("El nombre y/o la descripción están vacíos");
        }
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        return categoriaRepository.save(categoria);
    }
    public void eliminarCategoria(Long id) throws ResourceNotFoundException, BadRequestException {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isPresent()){
            Categoria categoriaExistente = categoria.get();
            List<Producto> productosAsociados = productoRepository.findByCategoria(categoriaExistente);

            if(!productosAsociados.isEmpty()) {
                throw new BadRequestException("La categoria con el ID:" + id + " esta asociada a uno o mas productos");
            }

            categoriaRepository.delete(categoriaExistente);
        } else {
            throw new ResourceNotFoundException("No se encontró la categoria con el ID: " + id);
        }
    }

}
