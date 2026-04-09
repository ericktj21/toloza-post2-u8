package com.example.inventariocqrs.adapter.web;

import com.example.inventariocqrs.command.AgregarProductoCommand;
import com.example.inventariocqrs.command.ActualizarStockCommand;
import com.example.inventariocqrs.command.EliminarProductoCommand;
import com.example.inventariocqrs.command.handler.AgregarProductoHandler;
import com.example.inventariocqrs.command.handler.ActualizarStockHandler;
import com.example.inventariocqrs.command.handler.EliminarProductoHandler;
import com.example.inventariocqrs.domain.ProductoId;
import com.example.inventariocqrs.query.BuscarProductoQuery;
import com.example.inventariocqrs.query.ListarProductosQuery;
import com.example.inventariocqrs.query.handler.BuscarProductoQueryHandler;
import com.example.inventariocqrs.query.handler.ListarProductosQueryHandler;
import com.example.inventariocqrs.query.model.ProductoView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST: Orquesta CommandHandlers y QueryHandlers
 * Separa explícitamente rutas de COMANDO (POST/PATCH/DELETE) de CONSULTA (GET)
 * 
 * Stack de Escritura (COMANDO):
 *   - POST /api/inventario/productos
 *   - PATCH /api/inventario/productos/{id}/stock
 *   - DELETE /api/inventario/productos/{id}
 *
 * Stack de Lectura (CONSULTA):
 *   - GET /api/inventario/productos
 *   - GET /api/inventario/productos/{id}
 */
@RestController
@RequestMapping("/api/inventario")
public class ProductoController {

    // ──────────────────────────────────────────
    // ──── COMMAND HANDLERS (Escritura) ────────
    // ──────────────────────────────────────────
    private final AgregarProductoHandler agregarProductoHandler;
    private final ActualizarStockHandler actualizarStockHandler;
    private final EliminarProductoHandler eliminarProductoHandler;

    // ──────────────────────────────────────────
    // ──── QUERY HANDLERS (Lectura) ───────────
    // ──────────────────────────────────────────
    private final BuscarProductoQueryHandler buscarProductoQueryHandler;
    private final ListarProductosQueryHandler listarProductosQueryHandler;

    public ProductoController(
            AgregarProductoHandler agregarProductoHandler,
            ActualizarStockHandler actualizarStockHandler,
            EliminarProductoHandler eliminarProductoHandler,
            BuscarProductoQueryHandler buscarProductoQueryHandler,
            ListarProductosQueryHandler listarProductosQueryHandler
    ) {
        this.agregarProductoHandler = agregarProductoHandler;
        this.actualizarStockHandler = actualizarStockHandler;
        this.eliminarProductoHandler = eliminarProductoHandler;
        this.buscarProductoQueryHandler = buscarProductoQueryHandler;
        this.listarProductosQueryHandler = listarProductosQueryHandler;
    }

    // ══════════════════════════════════════════════════════════
    // RUTAS DE COMANDO: Modifican el estado del sistema
    // ══════════════════════════════════════════════════════════

    /**
     * POST /api/inventario/productos
     * Crea un nuevo producto mediante AgregarProductoCommand
     * 
     * @param cmd comando con nombre, categoría, precio, stock inicial
     * @return mapa con el ID del producto creado
     */
    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> agregarProducto(@RequestBody AgregarProductoCommand cmd) {
        ProductoId productoId = agregarProductoHandler.handle(cmd);
        return Map.of(
            "productoId", productoId.toString(),
            "mensaje", "Producto creado exitosamente"
        );
    }

    /**
     * PATCH /api/inventario/productos/{id}/stock
     * Actualiza el stock de un producto mediante ActualizarStockCommand
     * 
     * @param id identificador del producto
     * @param cmd comando con delta (positivo o negativo) y motivo
     * @return mapa con mensaje de confirmación y nuevo stock
     */
    @PatchMapping("/productos/{id}/stock")
    public Map<String, Object> actualizarStock(
            @PathVariable String id,
            @RequestBody ActualizarStockCommand cmd
    ) {
        String mensaje = actualizarStockHandler.handle(
            new ActualizarStockCommand(id, cmd.delta(), cmd.motivo())
        );
        return Map.of(
            "mensaje", mensaje,
            "productoId", id
        );
    }

    /**
     * DELETE /api/inventario/productos/{id}
     * Elimina un producto mediante EliminarProductoCommand
     * 
     * @param id identificador del producto
     * @return mapa con mensaje de confirmación
     */
    @DeleteMapping("/productos/{id}")
    public Map<String, String> eliminarProducto(@PathVariable String id) {
        String mensaje = eliminarProductoHandler.handle(
            new EliminarProductoCommand(id)
        );
        return Map.of("mensaje", mensaje);
    }

    // ══════════════════════════════════════════════════════════
    // RUTAS DE CONSULTA: Solo lectura, sin efectos secundarios
    // ══════════════════════════════════════════════════════════

    /**
     * GET /api/inventario/productos
     * Lista todos los productos con modelo de lectura optimizado
     * 
     * @param soloDisponibles si es true, solo retorna productos con stock > 0
     * @return lista de ProductoView con estado calculado
     */
    @GetMapping("/productos")
    public List<ProductoView> listarProductos(
            @RequestParam(defaultValue = "false") boolean soloDisponibles
    ) {
        return listarProductosQueryHandler.handle(
            new ListarProductosQuery(soloDisponibles)
        );
    }

    /**
     * GET /api/inventario/productos/{id}
     * Busca un producto específico con modelo de lectura optimizado
     * 
     * @param id identificador del producto
     * @return ProductoView con estado calculado
     */
    @GetMapping("/productos/{id}")
    public ProductoView buscarProducto(@PathVariable String id) {
        return buscarProductoQueryHandler.handle(
            new BuscarProductoQuery(id)
        );
    }
}
