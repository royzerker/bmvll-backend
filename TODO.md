# TODO — Fase 1 (bmvll-backend)

Checklist de lo que falta implementar en esta primera fase del backend (catálogo + préstamo/devolución), antes de pasar a historial completo, PRs y release (APF2).

## 1. Modelamiento de datos
- [x] Definir colecciones `usuarios`, `categorias`, `libros`, `ejemplares`, `prestamos`, `multas`, `reservas` (ver [`docs/MODELADO.md`](./docs/MODELADO.md)).
- [x] Roles integrados en `usuarios.rol` (`ADMIN`, `BIBLIOTECARIO`, `SOCIO`) desde el diseño — no requiere migración futura.
- [x] Catálogo (`libros`) separado del inventario físico (`ejemplares`) para que sea escalable (multi-copia, multi-sede).
- [ ] **Definir si esta fase implementa login real (Spring Security)** o si `rol`/`passwordHash` quedan listos en el modelo pero sin autenticación activa todavía. *(pendiente de confirmar alcance)*

## 2. Capa de dominio (Java)
- [ ] `Usuario` (`@Document("usuarios")`)
- [ ] `Categoria` (`@Document("categorias")`)
- [ ] `Libro` (`@Document("libros")`)
- [ ] `Ejemplar` (`@Document("ejemplares")`)
- [ ] `Prestamo` (`@Document("prestamos")`)
- [ ] `Multa` (`@Document("multas")`)
- [ ] `Reserva` (`@Document("reservas")`)
- [ ] Enums: `RolUsuario`, `EstadoUsuario`, `EstadoEjemplar`, `CondicionEjemplar`, `EstadoPrestamo`, `MotivoMulta`, `EstadoMulta`, `EstadoReserva`

## 3. Repositorios (Spring Data MongoDB)
- [ ] `UsuarioRepository`
- [ ] `CategoriaRepository`
- [ ] `LibroRepository`
- [ ] `EjemplarRepository` (query por `libroId` + `estado`)
- [ ] `PrestamoRepository` (query por `usuarioId`, por `estado`)
- [ ] `MultaRepository`, `ReservaRepository` — cuando se implementen esas features

## 4. Servicios (reglas de negocio)
- [ ] `LibroService`: alta de libro, listar catálogo con disponibilidad (`count(ejemplares DISPONIBLE)` por libro)
- [ ] `PrestamoService`: registrar préstamo (busca ejemplar `DISPONIBLE`, lo marca `PRESTADO`), registrar devolución (libera el ejemplar, marca `prestamo` como `DEVUELTO`)
- [ ] Cálculo de `ATRASADO` al consultar (`fechaDevolucionEsperada < hoy` y `estado = ACTIVO`)
- [ ] `MultaService`, `ReservaService` — fuera de esta fase, el modelo ya las soporta

## 5. Endpoints REST
- [ ] `GET /api/libros` — catálogo con disponibilidad calculada
- [ ] `POST /api/prestamos` — registrar préstamo
- [ ] `PATCH /api/prestamos/{id}/devolucion` — registrar devolución
- [ ] `GET /api/usuarios/{id}/prestamos` — historial (APF2, pero puede dejarse listo si el modelo ya lo soporta)

## 6. Seguridad / roles
- [x] Modelo ya soporta roles (`usuarios.rol`) y tiene `passwordHash` reservado.
- [ ] Definir si esta fase requiere login (Spring Security) o si es de acceso libre (solo bibliotecario usa el sistema desde un panel interno, sin autenticación todavía)

## 7. Pruebas
- [ ] Test de contexto (`BackendApplicationTests`) — ya generado por Initializr
- [ ] Test de `PrestamoService` (caso: libro sin disponibilidad → debe rechazar préstamo)

## 8. Fuera de alcance de esta fase (queda para APF2)
- Historial de préstamos por usuario en frontend
- Pull Requests como flujo de integración
- Release identificable
- Tablero de gestión de tareas
