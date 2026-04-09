# Inventario CQRS - Patrón Arquitectónico

Sistema de Gestión de Inventario implementando el patrón **CQRS (Command Query Responsibility Segregation)** en Java con Spring Boot 3.x.

## 📋 Descripción del Proyecto

Este proyecto demuestra la implementación completa del patrón CQRS, separando explícitamente el stack de **escritura** (CommandHandlers) del stack de **lectura** (QueryHandlers). El objetivo es evidenciar cómo CQRS permite optimizar independientemente los modelos de lectura y escritura.

### Stack de Escritura (Commands)
- **AgregarProductoCommand**: Crear nuevos productos
- **ActualizarStockCommand**: Modificar el stock de inventario
- **EliminarProductoCommand**: Eliminar productos
- Utiliza la entidad de dominio `Producto` con lógica de negocio completa

### Stack de Lectura (Queries)
- **BuscarProductoQuery**: Consultar un producto específico
- **ListarProductosQuery**: Listar todos los productos con filtros
- Utiliza el modelo optimizado `ProductoView` (DTO) para presentación

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    REST Controller                          │
├───────────────────────┬─────────────────────────────────────┤
│    COMANDO (POST/     │        QUERY (GET)                  │
│    PATCH/DELETE)      │                                     │
├───────────────────────┼─────────────────────────────────────┤
│  CommandHandlers      │      QueryHandlers                  │
│  - AgregarProducto    │  - BuscarProducto                   │
│  - ActualizarStock    │  - ListarProductos                  │
│  - EliminarProducto   │                                     │
├───────────────────────┼─────────────────────────────────────┤
│ Dominio (Lógica)      │  Modelo Lectura (Presentación)     │
│ - Producto            │  - ProductoView                     │
│ - ProductoId          │  - calcularEstado()                 │
│ - Validaciones        │                                     │
├───────────────────────┼─────────────────────────────────────┤
│  WriteRepository      │     ReadRepository                  │
│  (JPA)                │     (JPA)                           │
├───────────────────────┴─────────────────────────────────────┤
│              Base de Datos (H2 In-Memory)                   │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Requisitos

- **Java**: JDK 17 o superior
- **Maven**: 3.8+
- **Spring Boot**: 3.2.0
- **Dependencias**:
  - Spring Web
  - Spring Data JPA
  - H2 Database
  - Validation

## 📦 Estructura de Carpetas

```
toloza-post2-u8/
├── pom.xml
├── README.md
├── src/main/java/com/example/inventariocqrs/
│   ├── InventarioCqrsApplication.java
│   ├── domain/
│   │   ├── Producto.java               ← Entidad de dominio
│   │   ├── ProductoId.java             ← Value Object
│   │   ├── ProductoNotFoundException.java
│   │   └── StockInsuficienteException.java
│   ├── command/
│   │   ├── AgregarProductoCommand.java
│   │   ├── ActualizarStockCommand.java
│   │   ├── EliminarProductoCommand.java
│   │   ├── handler/
│   │   │   ├── AgregarProductoHandler.java
│   │   │   ├── ActualizarStockHandler.java
│   │   │   └── EliminarProductoHandler.java
│   │   └── repository/
│   │       └── ProductoWriteRepository.java
│   ├── query/
│   │   ├── BuscarProductoQuery.java
│   │   ├── ListarProductosQuery.java
│   │   ├── handler/
│   │   │   ├── BuscarProductoQueryHandler.java
│   │   │   └── ListarProductosQueryHandler.java
│   │   ├── model/
│   │   │   └── ProductoView.java
│   │   └── repository/
│   │       └── ProductoReadRepository.java
│   └── adapter/
│       ├── web/
│       │   └── ProductoController.java
│       └── exception/
│           └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties
```

## 🔧 Cómo Ejecutar

### 1. Compilar el proyecto
```bash
mvn clean compile
```

### 2. Crear el JAR ejecutable
```bash
mvn package -DskipTests
```

### 3. Ejecutar la aplicación
```bash
java -jar target/inventario-cqrs-1.0.0.jar
```

La aplicación estará disponible en: **http://localhost:8081**

## 🧪 Endpoints de API

### COMANDO: Crear Producto
```bash
POST /api/inventario/productos
Content-Type: application/json

{
  "nombre": "Laptop Dell XPS",
  "categoria": "Electronica",
  "precioUnitario": 1200.50,
  "stockInicial": 10
}
```

**Respuesta (201 Created):**
```json
{
  "mensaje": "Producto creado exitosamente",
  "productoId": "b8d95206-004a-495f-bdb6-fbab164165be"
}
```

### COMANDO: Actualizar Stock
```bash
PATCH /api/inventario/productos/{id}/stock
Content-Type: application/json

{
  "productoId": "b8d95206-004a-495f-bdb6-fbab164165be",
  "delta": 5,
  "motivo": "compra"
}
```

Delta positivo: incrementa stock
Delta negativo: reduce stock

**Respuesta (200 OK):**
```json
{
  "mensaje": "Stock actualizado. Nuevo stock: 15. Motivo: compra",
  "productoId": "b8d95206-004a-495f-bdb6-fbab164165be"
}
```

### COMANDO: Eliminar Producto
```bash
DELETE /api/inventario/productos/{id}
```

**Respuesta (200 OK):**
```json
{
  "mensaje": "Producto b8d95206-004a-495f-bdb6-fbab164165be eliminado correctamente"
}
```

### QUERY: Listar Productos
```bash
GET /api/inventario/productos?soloDisponibles=false
```

**Respuesta (200 OK):**
```json
[
  {
    "id": "b8d95206-004a-495f-bdb6-fbab164165be",
    "nombre": "Laptop Dell XPS",
    "categoria": "Electronica",
    "precioUnitario": 1200.50,
    "stockDisponible": 12,
    "estadoStock": "DISPONIBLE"
  },
  {
    "id": "ba4da452-e7e4-423c-af87-087a37a15646",
    "nombre": "Mouse Logitech",
    "categoria": "Accesorios",
    "precioUnitario": 25.90,
    "stockDisponible": 3,
    "estadoStock": "BAJO"
  }
]
```

### QUERY: Buscar Producto Específico
```bash
GET /api/inventario/productos/{id}
```

**Respuesta (200 OK):**
```json
{
  "id": "b8d95206-004a-495f-bdb6-fbab164165be",
  "nombre": "Laptop Dell XPS",
  "categoria": "Electronica",
  "precioUnitario": 1200.50,
  "stockDisponible": 12,
  "estadoStock": "DISPONIBLE"
}
```

## ⚠️ Errores Posibles

### Stock Insuficiente
```bash
PATCH con delta que excede stock disponible
Respuesta (400 Bad Request):
{
  "error": "Stock Insuficiente",
  "mensaje": "Stock insuficiente para producto...",
  "status": 400,
  "stockActual": 10,
  "unidadesSolicitadas": 15
}
```

### Producto No Encontrado
```bash
GET con ID que no existe
Respuesta (404 Not Found):
{
  "error": "Producto No Encontrado",
  "mensaje": "Producto no encontrado: id-inexistente",
  "status": 404
}
```

## 📌 Estados del Stock

El modelo `ProductoView` calcula automáticamente el estado:
- **DISPONIBLE**: stock >= 5
- **BAJO**: 0 < stock < 5
- **AGOTADO**: stock == 0

## 🔑 Características Principales de CQRS

✅ **Separación clara**: Commands y Queries en paquetes diferentes
✅ **Responsabilidad única**: Cada handler tiene un único propósito
✅ **Lógica de dominio**: Concentrada en la entidad `Producto`
✅ **Modelo optimizado**: `ProductoView` para presentación eficiente
✅ **Manejo de excepciones**: GlobalExceptionHandler con respuestas HTTP apropiad
✅ **Inmutabilidad**: Comandos y Queries son records (inmutables)
✅ **Validación**: Reglas de negocio en el dominio

## 💾 Base de Datos

Se utiliza **H2 In-Memory** para este laboratorio:
- **URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: (vacía)
- **Consola H2**: http://localhost:8081/h2-console

## 📊 Commits del Proyecto

1. **Commit 1**: Estructura Maven base y configuración Spring Boot
2. **Commit 2**: Domain layer - Producto entity con lógica de negocio
3. **Commit 3**: Command side - Comandos y handlers
4. **Commit 4**: Query side - Queries, handlers y modelo de lectura
5. **Commit 5**: REST controller - Orquestación de comandos y consultas
6. **Commit 6**: Fix - Corregir import en Producto.java
7. **Commit 7**: Exception handler - Manejo global de excepciones

## ✨ Conclusiones

Este proyecto implementa CQRS demostrando:

1. **Separación de intereses**: El stack de escritura y lectura son completamente independientes
2. **Escalabilidad**: Cada lado puede optimizarse según su propósito
3. **Mantenibilidad**: El código es más organizado y fácil de entender
4. **Testabilidad**: Cada componente (handler, command, query) es fácil de probar

El patrón CQRS es especialmente útil en sistemas complejos donde los modelos de lectura y escritura tienen requisitos muy diferentes.

---

**Autor**: Ingeniería de Sistemas 2026  
**Patrón**: CQRS (Command Query Responsibility Segregation)  
**Tecnologías**: Java 17, Spring Boot 3.2, Maven, H2
