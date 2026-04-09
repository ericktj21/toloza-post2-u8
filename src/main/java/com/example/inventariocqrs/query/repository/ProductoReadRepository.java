package com.example.inventariocqrs.query.repository;

import com.example.inventariocqrs.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Lectura para Productos
 * Utilizado exclusivamente por los QueryHandlers
 * En un sistema CQRS real, podría leer desde una base de datos de lectura optimizada
 */
@Repository
public interface ProductoReadRepository extends JpaRepository<Producto, String> {
    Optional<Producto> findById(String id);
}
