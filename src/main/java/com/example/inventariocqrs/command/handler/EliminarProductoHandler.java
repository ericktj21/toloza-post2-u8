package com.example.inventariocqrs.command.handler;

import com.example.inventariocqrs.command.EliminarProductoCommand;
import com.example.inventariocqrs.command.repository.ProductoWriteRepository;
import com.example.inventariocqrs.domain.ProductoNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Command Handler: Procesa el comando de eliminar un producto
 * Responsabilidad única: eliminar un Producto del inventario
 */
@Component
public class EliminarProductoHandler {
    private final ProductoWriteRepository writeRepository;

    public EliminarProductoHandler(ProductoWriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    public String handle(EliminarProductoCommand cmd) {
        // Verificar que el producto existe
        if (!writeRepository.existsById(cmd.productoId())) {
            throw new ProductoNotFoundException(cmd.productoId());
        }

        // Eliminar de la base de datos
        writeRepository.deleteById(cmd.productoId());

        return String.format("Producto %s eliminado correctamente", cmd.productoId());
    }
}
