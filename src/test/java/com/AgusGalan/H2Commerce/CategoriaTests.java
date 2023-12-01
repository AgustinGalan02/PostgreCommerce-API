package com.AgusGalan.H2Commerce;

import com.AgusGalan.H2Commerce.exceptions.BadRequestException;
import com.AgusGalan.H2Commerce.exceptions.ResourceNotFoundException;
import com.AgusGalan.H2Commerce.persistence.entities.Categoria;
import com.AgusGalan.H2Commerce.persistence.repository.CategoriaRepository;
import com.AgusGalan.H2Commerce.service.CategoriaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoriaTests {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Before
    public void CargarDataSet() {
        Categoria categoria = new Categoria();
        categoria.setDescripcion("categoriaPrueba");
        categoria.setNombre("categoriaPrueba");
        categoriaRepository.save(categoria);
    }

    @Test
    public void testAltaCategoria() throws BadRequestException, ResourceNotFoundException {
        Categoria categoria = new Categoria();
        categoria.setDescripcion("categoriaPrueba");
        categoria.setNombre("categoriaPrueba");
        Categoria categoriaguardada = categoriaService.guardarCategoria(categoria);
        Long CategoriaId = categoria.getId();
        categoriaService.eliminarCategoria(CategoriaId);

    }

    @Test
    public void testBajaCategoria() throws ResourceNotFoundException, BadRequestException {
        Categoria categoria = new Categoria();
        categoria.setDescripcion("categoriaPrueba");
        categoria.setNombre("categoriaPrueba");

            Categoria categoriaguardada = categoriaService.guardarCategoria(categoria);
            Long CategoriaId = categoria.getId();
            categoriaService.eliminarCategoria(CategoriaId);
            Categoria categoriaEliminada = categoriaRepository.findById(CategoriaId).orElse(null);
            Assert.assertNull(categoriaEliminada);


    }
}




