package com.example.inventariocqrs.command;

import java.math.BigDecimal;

/**
 * Comando: Agregar un nuevo producto al inventario
 * Objeto inmutable que representa la intención de crear un producto
 */
public record AgregarProductoCommand(
    String nombre,
    String categoria,
    BigDecimal precioUnitario,
    int stockInicial
) {}
