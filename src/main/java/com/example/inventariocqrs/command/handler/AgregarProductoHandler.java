package com.example.inventariocqrs.command.handler;

import com.example.inventariocqrs.command.AgregarProductoCommand;
import com.example.inventariocqrs.command.repository.ProductoWriteRepository;
import com.example.inventariocqrs.domain.Producto;
import com.example.inventariocqrs.domain.ProductoId;
import org.springframework.stereotype.Component;

/**
 * Command Handler: Procesa el comando de agregar un nuevo producto
 * Responsabilidad única: crear y persistir un nuevo Producto
 */
@Component
public class AgregarProductoHandler {
    private final ProductoWriteRepository writeRepository;

    public AgregarProductoHandler(ProductoWriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    public ProductoId handle(AgregarProductoCommand cmd) {
        // Crear entidad de dominio con lógica de negocio
        Producto producto = new Producto(
            ProductoId.nuevo(),
            cmd.nombre(),
            cmd.categoria(),
            cmd.precioUnitario(),
            cmd.stockInicial()
        );

        // Persistir en base de datos
        writeRepository.save(producto);

        return producto.getId();
    }
}
