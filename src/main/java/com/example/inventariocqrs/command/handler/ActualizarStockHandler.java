package com.example.inventariocqrs.command.handler;

import com.example.inventariocqrs.command.ActualizarStockCommand;
import com.example.inventariocqrs.command.repository.ProductoWriteRepository;
import com.example.inventariocqrs.domain.Producto;
import com.example.inventariocqrs.domain.ProductoId;
import com.example.inventariocqrs.domain.ProductoNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Command Handler: Procesa el comando de actualizar el stock
 * Responsabilidad única: modificar el stock aplicando lógica de dominio
 */
@Component
public class ActualizarStockHandler {
    private final ProductoWriteRepository writeRepository;

    public ActualizarStockHandler(ProductoWriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    public String handle(ActualizarStockCommand cmd) {
        // Buscar el producto existente
        Producto producto = writeRepository.findById(cmd.productoId())
            .orElseThrow(() -> new ProductoNotFoundException(cmd.productoId()));

        // Aplicar lógica de dominio según el delta
        if (cmd.delta() > 0) {
            producto.incrementarStock(cmd.delta());
        } else if (cmd.delta() < 0) {
            producto.reducirStock(Math.abs(cmd.delta()));
        }

        // Persistir cambios
        writeRepository.save(producto);

        return String.format("Stock actualizado. Nuevo stock: %d. Motivo: %s",
            producto.getStockDisponible(), cmd.motivo());
    }
}
