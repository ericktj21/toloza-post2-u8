package com.example.inventariocqrs.command.repository;

import com.example.inventariocqrs.domain.Producto;
import com.example.inventariocqrs.domain.ProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Escritura para Productos
 * Utilizado exclusivamente por los CommandHandlers
 * Persiste cambios en el modelo de dominio
 */
@Repository
public interface ProductoWriteRepository extends JpaRepository<Producto, String> {
    Optional<Producto> findById(String id);
}
