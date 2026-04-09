package com.example.inventariocqrs.command;

/**
 * Comando: Eliminar un producto del inventario
 */
public record EliminarProductoCommand(
    String productoId
) {}
