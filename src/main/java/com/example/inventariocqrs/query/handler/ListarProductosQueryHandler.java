package com.example.inventariocqrs.query.handler;

import com.example.inventariocqrs.query.ListarProductosQuery;
import com.example.inventariocqrs.query.model.ProductoView;
import com.example.inventariocqrs.query.repository.ProductoReadRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Query Handler: Procesa la consulta de listar productos
 * Responsabilidad única: recuperar, filtrar y transformar Productos a ProductoViews
 */
@Component
public class ListarProductosQueryHandler {
    private final ProductoReadRepository readRepository;

    public ListarProductosQueryHandler(ProductoReadRepository readRepository) {
        this.readRepository = readRepository;
    }

    public List<ProductoView> handle(ListarProductosQuery query) {
        return readRepository.findAll().stream()
            .filter(producto -> !query.soloDisponibles() || producto.getStockDisponible() > 0)
            .map(producto -> new ProductoView(
                producto.getId().toString(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecioUnitario(),
                producto.getStockDisponible(),
                ProductoView.calcularEstado(producto.getStockDisponible())
            ))
            .collect(Collectors.toList());
    }
}
