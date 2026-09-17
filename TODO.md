# TODO — Fase 1 (bmvll-backend)

Checklist de lo que falta implementar en esta primera fase del backend (catálogo + préstamo/devolución), antes de pasar a historial completo, PRs y release (APF2).

## 1. Modelamiento de datos
- [x] Definir colecciones `users`, `categories`, `books`, `copies`, `loans`, `fines`, `reservations` — nombres y campos en inglés (ver [`docs/MODELADO.md`](./docs/MODELADO.md)).
- [x] Roles integrados en `users.role` (`ADMIN`, `LIBRARIAN`, `MEMBER`) desde el diseño — no requiere migración futura.
- [x] Catálogo (`books`) separado del inventario físico (`copies`) para que sea escalable (multi-copia, multi-sede).
- [x] **Decisión: staff (`ADMIN`/`LIBRARIAN`) y socios comparten la colección `users`** — no se crea una colección `staff` separada; el acceso al back-office se modela con `role`, no con una entidad distinta.
- [ ] **Definir si esta fase implementa login real (Spring Security)** o si `role`/`passwordHash` quedan listos en el modelo pero sin autenticación activa todavía. *(pendiente de confirmar alcance — independiente de la decisión anterior)*

## 2. Capa de dominio (Java)
- [ ] `User` (`@Document("users")`)
- [ ] `Category` (`@Document("categories")`)
- [ ] `Book` (`@Document("books")`)
- [ ] `Copy` (`@Document("copies")`)
- [ ] `Loan` (`@Document("loans")`)
- [ ] `Fine` (`@Document("fines")`)
- [ ] `Reservation` (`@Document("reservations")`)
- [ ] Enums: `UserRole`, `UserStatus`, `CopyStatus`, `CopyCondition`, `LoanStatus`, `FineReason`, `FineStatus`, `ReservationStatus`

## 3. Repositorios (Spring Data MongoDB)
- [ ] `UserRepository`
- [ ] `CategoryRepository`
- [ ] `BookRepository`
- [ ] `CopyRepository` (query por `bookId` + `status`)
- [ ] `LoanRepository` (query por `userId`, por `status`)
- [ ] `FineRepository`, `ReservationRepository` — cuando se implementen esas features

## 4. Servicios (reglas de negocio)
- [ ] `BookService`: alta de libro, listar catálogo con disponibilidad (`count(copies AVAILABLE)` por libro)
- [ ] `LoanService`: registrar préstamo (busca copy `AVAILABLE`, la marca `LOANED`), registrar devolución (libera la copy, marca `loan` como `RETURNED`)
- [ ] Cálculo de `OVERDUE` al consultar (`dueDate < hoy` y `status = ACTIVE`)
- [ ] `FineService`, `ReservationService` — fuera de esta fase, el modelo ya las soporta

## 5. Endpoints REST
- [ ] `GET /api/books` — catálogo con disponibilidad calculada
- [ ] `POST /api/loans` — registrar préstamo
- [ ] `PATCH /api/loans/{id}/return` — registrar devolución
- [ ] `GET /api/users/{id}/loans` — historial (APF2, pero puede dejarse listo si el modelo ya lo soporta)

## 6. Seguridad / roles
- [x] Modelo ya soporta roles (`users.role`) y tiene `passwordHash` reservado.
- [ ] Definir si esta fase requiere login (Spring Security) o si es de acceso libre (solo bibliotecario usa el sistema desde un panel interno, sin autenticación todavía)

## 7. Pruebas
- [ ] Test de contexto (`BackendApplicationTests`) — ya generado por Initializr
- [ ] Test de `LoanService` (caso: libro sin disponibilidad → debe rechazar préstamo)

## 8. Fuera de alcance de esta fase (queda para APF2)
- Historial de préstamos por usuario en frontend
- Pull Requests como flujo de integración
- Release identificable
- Tablero de gestión de tareas
