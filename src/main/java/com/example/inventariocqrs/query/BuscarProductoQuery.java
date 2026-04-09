package com.example.inventariocqrs.query;

/**
 * Query: Buscar un producto específico
 * Objeto inmutable que representa una consulta de lectura
 */
public record BuscarProductoQuery(
    String productoId
) {}
