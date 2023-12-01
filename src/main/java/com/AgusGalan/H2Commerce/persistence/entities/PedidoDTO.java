package com.AgusGalan.H2Commerce.persistence.entities;

import java.util.List;

public class PedidoDTO {
    private Long id;
    private List<ProductoDTO> productos;

    //---------------------------------------------//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }
}