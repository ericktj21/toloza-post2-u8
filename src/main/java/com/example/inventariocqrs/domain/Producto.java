package com.example.inventariocqrs.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad de Dominio: Producto
 * Contiene la lógica de negocio relacionada con productos e inventario.
 * Solo el stack de escritura (command) utiliza directamente esta clase.
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "stock_disponible", nullable = false)
    private int stockDisponible;

    // Constructor vacío para JPA
    protected Producto() {
    }

    /**
     * Constructor del Producto con validación de reglas de negocio
     */
    public Producto(ProductoId id, String nombre, String categoria, BigDecimal precio, int stockInicial) {
        // Validaciones de dominio
        if (id == null) {
            throw new IllegalArgumentException("ProductoId es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre es obligatorio");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio debe ser positivo");
        }
        if (stockInicial < 0) {
            throw new IllegalArgumentException("Stock inicial no puede ser negativo");
        }

        this.id = id.val();
        this.nombre = nombre;
        this.categoria = categoria != null ? categoria : "";
        this.precioUnitario = precio;
        this.stockDisponible = stockInicial;
    }

    /**
     * Incrementa el stock disponible
     */
    public void incrementarStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Unidades a incrementar deben ser positivas");
        }
        this.stockDisponible += unidades;
    }

    /**
     * Reduce el stock disponible, validando que haya suficiente
     */
    public void reducirStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Unidades a reducir deben ser positivas");
        }
        if (unidades > this.stockDisponible) {
            throw new StockInsuficienteException(
                ProductoId.de(this.id),
                this.stockDisponible,
                unidades
            );
        }
        this.stockDisponible -= unidades;
    }

    // Getters
    public ProductoId getId() {
        return ProductoId.de(this.id);
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", stockDisponible=" + stockDisponible +
                '}';
    }
}
