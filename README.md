#  Pastelería Mil Sabores - Backend REST API

Sistema Backend desarrollado con **Spring Boot 3** y **Java 21**, conectado a base de datos relacional **MySQL 8.0** y orquestado mediante **Docker Compose**. Implementa seguridad sin estado (**Stateless**) con **JWT (JSON Web Tokens)**, arquitectura por capas y documentación interactiva de endpoints con **OpenAPI / Swagger UI**.

---

##  Integrantes del Equipo

- **Lucas Guerra**
- **Pedro García**

---

##  Accesos Directos del Sistema (Local / Docker)

Una vez iniciado el entorno con Docker Compose, todos los servicios quedan disponibles en las siguientes URLs:

| Servicio | URL / Host | Descripción | Credenciales / Acceso |
| :--- | :--- | :--- | :--- |
| **Frontend Web (React)** | [http://localhost:3000](http://localhost:3000) | Aplicación cliente (catálogo, carrito, pedidos y panel) | Roles Cliente, Vendedor y Admin |
| **Documentación Swagger UI** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | Pruebas interactivas de todas las APIs REST | Probar GET, POST, PUT, DELETE |
| **API Base Backend** | [http://localhost:8080/api/v1](http://localhost:8080/api/v1) | Endpoint base de la API REST | Respuestas en formato JSON |
| **phpMyAdmin (BD Visual)** | [http://localhost:8081](http://localhost:8081) | Gestor web visual de base de datos MySQL | Usuario: `root` \| Pass: *(vacío)* |
| **Conexión MySQL Directa** | `localhost:3307` | Conexión externa (DBeaver, Workbench) | BD: `pasteleria_db` \| User: `root` |

> **Nota sobre puertos:** MySQL expone externamente el puerto `3307` en el host para prevenir conflictos con instalaciones locales preexistentes (como XAMPP en puerto `3306`), mientras que en la red interna de Docker se comunica nativamente en el puerto `3306`.

---

##  Stack Tecnológico

- **Lenguaje:** Java 21 (LTS)
- **Framework:** Spring Boot 3.3+
  - Spring Web (Controladores REST y gestión HTTP)
  - Spring Data JPA & Hibernate (Persistencia y mapeo relacional)
  - Spring Security 6 (Seguridad, filtros y autorización RBAC)
  - JWT (io.jsonwebtoken / jjwt) (Generación y validación de tokens Bearer)
  - Spring Boot Validation (Validación de DTOs en entradas)
- **Base de Datos:** MySQL 8.0
- **Documentación API:** OpenAPI 3 / Springdoc Swagger UI
- **Contenedores:** Docker & Docker Compose (Multi-stage builds)

---

##  Arquitectura del Backend

El proyecto implementa un patrón arquitectónico en capas para asegurar alta cohesión y bajo acoplamiento:

```
[ Cliente HTTP / Frontend ]
          │ (Peticiones JSON vía REST)
          ▼
┌─────────────────────────────────────────────────────────────┐
│ 1. Filtro de Seguridad (JwtFilter & SecurityConfig)         │
└─────────────────────────┬───────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Controladores (@RestController en /controller)           │
│    - AuthController, ProductoController, OrdenController... │
└─────────────────────────┬───────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────────┐
│ 3. Repositorios JPA (/repository)                           │
│    - Consultas declarativas y CRUD automático               │
└─────────────────────────┬───────────────────────────────────┘
                          ▼
┌─────────────────────────────────────────────────────────────┐
│ 4. Base de Datos Relacional (MySQL 8.0)                     │
└─────────────────────────────────────────────────────────────┘
```

---

##  Cómo Ejecutar el Proyecto

### Con Docker Compose

Asegúrate de tener **Docker Desktop** abierto y ejecuta desde la raíz de cualquiera de los repositorios:

```bash
docker compose up -d --build
```

Esto descargará y compilará automáticamente los 4 contenedores (`pasteleria-mysql`, `pasteleria-backend`, `pasteleria-frontend` y `pasteleria-phpmyadmin`).

Para detener los servicios:

```bash
docker compose down
```

---

##  Usuarios y Credenciales Semilla (DataInitializer)

El backend cuenta con un `DataInitializer` automático que puebla categorías, productos y los siguientes usuarios de prueba:

| Rol | Email / Usuario | Contraseña | Beneficio / Características |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@pasteleriamilsabores.cl` | `admin123` | Acceso total al panel administrativo |
| **VENDEDOR** | `vendedor@pasteleriamilsabores.cl` | `vendedor123` | Gestión de inventario y pedidos |
| **CLIENTE (Senior)** | `rosa.carrasco@gmail.com` | `rosa123` | 61 años ➔ Aplica 50% Descuento Senior |
| **CLIENTE (Duoc)** | `estudiante@duocuc.cl` | `duoc123` | Correo Duoc ➔ Aplica 50% Descuento Duoc |

---

##  Endpoints Principales

- **Autenticación:**
  - `POST /api/v1/auth/login` - Inicio de sesión y entrega de token JWT.
  - `POST /api/v1/auth/register` - Registro de nuevos usuarios clientes.
- **Productos & Categorías:**
  - `GET /api/v1/productos` - Catálogo completo de tortas y productos.
  - `GET /api/v1/productos/{id}` - Detalle de un producto específico.
  - `GET /api/v1/categorias` - Listado de categorías disponibles.
- **Órdenes y Ventas:**
  - `POST /api/v1/ordenes` - Creación y checkout de nueva orden de compra.
  - `GET /api/v1/ordenes` - Historial de compras (requiere autenticación).
- **Reportes:**
  - `GET /api/v1/reportes/dashboard` - Métricas para el panel de administración.
