package com.example.inventariocqrs.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un Producto
 */
public class ProductoId implements Serializable {
    private final String valor;

    private ProductoId(String valor) {
        this.valor = Objects.requireNonNull(valor);
    }

    public static ProductoId nuevo() {
        return new ProductoId(UUID.randomUUID().toString());
    }

    public static ProductoId de(String valor) {
        return new ProductoId(valor);
    }

    public String val() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoId that = (ProductoId) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
