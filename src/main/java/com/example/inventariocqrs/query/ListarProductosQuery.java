package com.example.inventariocqrs.query;

/**
 * Query: Listar todos los productos disponibles
 */
public record ListarProductosQuery(
    boolean soloDisponibles
) {}
