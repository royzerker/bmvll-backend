# TODO — Fase 1 (bmvll-backend)

Checklist de lo que falta implementar en esta primera fase del backend (catálogo + préstamo/devolución), antes de pasar a historial completo, PRs y release (APF2).

## 1. Modelamiento de datos
- [x] Definir colecciones `users`, `categories`, `books`, `copies`, `loans`, `fines`, `reservations` — nombres y campos en inglés (ver [`docs/MODELADO.md`](./docs/MODELADO.md)).
- [x] Roles integrados en `users.role` (`ADMIN`, `LIBRARIAN`, `MEMBER`) desde el diseño — no requiere migración futura.
- [x] Catálogo (`books`) separado del inventario físico (`copies`) para que sea escalable (multi-copia, multi-sede).
- [x] **Decisión: staff (`ADMIN`/`LIBRARIAN`) y socios comparten la colección `users`** — no se crea una colección `staff` separada; el acceso al back-office se modela con `role`, no con una entidad distinta.
- [x] **Decisión: login real con JWT (Spring Security), solo para `ADMIN`/`LIBRARIAN`** — `MEMBER` no tiene login en esta fase (ver rama `feature/jwt-auth`).

## 2. Capa de dominio (Java)
- [x] `User` (`@Document("users")`)
- [ ] `Category` (`@Document("categories")`)
- [ ] `Book` (`@Document("books")`)
- [ ] `Copy` (`@Document("copies")`)
- [ ] `Loan` (`@Document("loans")`)
- [ ] `Fine` (`@Document("fines")`)
- [ ] `Reservation` (`@Document("reservations")`)
- [x] Enums de `User`: `UserRole`, `UserStatus`, `DocumentType`
- [ ] Resto de enums: `CopyStatus`, `CopyCondition`, `LoanStatus`, `FineReason`, `FineStatus`, `ReservationStatus`

## 3. Repositorios (Spring Data MongoDB)
- [x] `UserRepository`
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
- [x] `POST /api/auth/login` — login de `ADMIN`/`LIBRARIAN`, devuelve JWT
- [x] `POST /api/auth/register` — solo `ADMIN`, crea cuentas `ADMIN`/`LIBRARIAN`
- [ ] `GET /api/books` — catálogo con disponibilidad calculada (queda público, sin JWT)
- [ ] `POST /api/loans` — registrar préstamo (requiere JWT `ADMIN`/`LIBRARIAN`)
- [ ] `PATCH /api/loans/{id}/return` — registrar devolución (requiere JWT `ADMIN`/`LIBRARIAN`)
- [ ] `GET /api/users/{id}/loans` — historial (APF2, pero puede dejarse listo si el modelo ya lo soporta)

## 6. Seguridad / roles
- [x] Modelo ya soporta roles (`users.role`) y tiene `passwordHash` reservado.
- [x] Login con JWT (Spring Security) para `ADMIN`/`LIBRARIAN`; `MEMBER` sin login por ahora.
- [x] `SecurityConfig`: `/api/auth/login` público, `GET /api/books|categories` público, `/api/auth/register` solo `ADMIN`, resto requiere `ADMIN`/`LIBRARIAN`.
- [x] `JwtAuthenticationFilter` valida el token y re-verifica `status = ACTIVE` contra la BD en cada request (no confía solo en los claims).
- [x] Bootstrap de un ADMIN inicial vía `.env` (`BOOTSTRAP_ADMIN_EMAIL`/`BOOTSTRAP_ADMIN_PASSWORD`) — solo dev/local, deshabilitado en el perfil `test`.

## 7. Pruebas
- [x] Test de contexto (`BackendApplicationTests`) — perfil `test` evita tocar Mongo real al arrancar.
- [x] `JwtServiceTest` — genera/valida token, rechaza firma distinta y token expirado.
- [ ] Test de `LoanService` (caso: libro sin disponibilidad → debe rechazar préstamo)

## 8. Fuera de alcance de esta fase (queda para APF2)
- Historial de préstamos por usuario en frontend
- Pull Requests como flujo de integración
- Release identificable
- Tablero de gestión de tareas
