package com.example.inventariocqrs.query.handler;

import com.example.inventariocqrs.domain.ProductoNotFoundException;
import com.example.inventariocqrs.query.BuscarProductoQuery;
import com.example.inventariocqrs.query.model.ProductoView;
import com.example.inventariocqrs.query.repository.ProductoReadRepository;
import org.springframework.stereotype.Component;

/**
 * Query Handler: Procesa la consulta de búsqueda de un producto
 * Responsabilidad única: recuperar y transformar un Producto a ProductoView
 */
@Component
public class BuscarProductoQueryHandler {
    private final ProductoReadRepository readRepository;

    public BuscarProductoQueryHandler(ProductoReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    public ProductoView handle(BuscarProductoQuery query) {
        return readRepository.findById(query.productoId())
            .map(producto -> new ProductoView(
                producto.getId().toString(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecioUnitario(),
                producto.getStockDisponible(),
                ProductoView.calcularEstado(producto.getStockDisponible())
            ))
            .orElseThrow(() -> new ProductoNotFoundException(query.productoId()));
    }
}
