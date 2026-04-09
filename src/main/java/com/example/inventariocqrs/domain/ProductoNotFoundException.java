package com.example.inventariocqrs.domain;

/**
 * Excepción lanzada cuando un producto no es encontrado
 */
public class ProductoNotFoundException extends RuntimeException {
    private final String productoId;

    public ProductoNotFoundException(String productoId) {
        super(String.format("Producto no encontrado: %s", productoId));
        this.productoId = productoId;
    }

    public String getProductoId() {
        return productoId;
    }
}
