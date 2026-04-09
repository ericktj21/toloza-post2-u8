package com.example.inventariocqrs.command;

/**
 * Comando: Actualizar el stock de un producto
 * delta > 0 = incrementar stock
 * delta < 0 = reducir stock
 */
public record ActualizarStockCommand(
    String productoId,
    int delta,
    String motivo
) {}
