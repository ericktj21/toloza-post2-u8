package com.example.inventariocqrs.domain;

/**
 * Excepción lanzada cuando se intenta reducir el stock por debajo del mínimo disponible
 */
public class StockInsuficienteException extends RuntimeException {
    private final ProductoId productoId;
    private final int stockActual;
    private final int unidadesSolicitadas;

    public StockInsuficienteException(ProductoId productoId, int stockActual, int unidadesSolicitadas) {
        super(String.format(
            "Stock insuficiente para producto %s. Stock disponible: %d, Solicitado: %d",
            productoId, stockActual, unidadesSolicitadas
        ));
        this.productoId = productoId;
        this.stockActual = stockActual;
        this.unidadesSolicitadas = unidadesSolicitadas;
    }

    public ProductoId getProductoId() {
        return productoId;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getUnidadesSolicitadas() {
        return unidadesSolicitadas;
    }
}
