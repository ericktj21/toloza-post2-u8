package com.example.inventariocqrs.query.model;

import java.math.BigDecimal;

/**
 * Modelo de Lectura Optimizado: ProductoView
 * DTO específicamente diseñado para presentación
 * No contiene lógica de negocio, solo lógica de presentación
 */
public record ProductoView(
    String id,
    String nombre,
    String categoria,
    BigDecimal precioUnitario,
    int stockDisponible,
    String estadoStock // "DISPONIBLE", "BAJO", "AGOTADO"
) {

    /**
     * Calcula el estado del stock para presentación
     */
    public static String calcularEstado(int stock) {
        if (stock == 0) {
            return "AGOTADO";
        } else if (stock < 5) {
            return "BAJO";
        } else {
            return "DISPONIBLE";
        }
    }
}
