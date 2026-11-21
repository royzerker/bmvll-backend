# bmvll-backend

![CI](https://github.com/royzerker/bmvll-backend/actions/workflows/ci.yml/badge.svg)

API REST del sistema de gestión de préstamos de la Biblioteca Municipal Mario Vargas Llosa. Spring Boot 4 + MongoDB.

## Estado del proyecto
Versión candidata para la etapa final. Incluye: login con JWT (ADMIN/LIBRARIAN), categorías, catálogo y búsqueda de libros, inventario de copias, préstamos/devoluciones y miembros. Pendiente: frontend en React.

## Entorno desplegado
[URL de la nube, por ejemplo https://bmvll-backend.onrender.com/api/books]

## Requisitos
Java 17+, Maven (o `./mvnw`) y una base MongoDB (local o Atlas).

## Variables de entorno
Copia `.env.example` a `.env` y completa:
`MONGODB_URI`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `BOOTSTRAP_ADMIN_EMAIL`, `BOOTSTRAP_ADMIN_PASSWORD`.

## Ejecutar en local
    ./mvnw spring-boot:run

## Pruebas
    ./mvnw test

## Docker
    docker build -t bmvll-backend .
    docker run -p 8080:8080 --env-file .env bmvll-backend

## CI/CD
- **CI** (`.github/workflows/ci.yml`): compila y ejecuta las pruebas en cada push y Pull Request.
- **Release** (`.github/workflows/release.yml`): al crear un tag `v*`, publica el `.jar` en GitHub Releases.
- **Despliegue**: automático en [plataforma] al integrar en `master`.


## Flujo de ramas

El proyecto usa 3 ramas principales:

| Rama          | Propósito                                              |
|---------------|---------------------------------------------------------|
| `master`      | Versión estable / release                              |
| `staging`     | Integración pre-producción, pruebas antes de `master`  |
| `development` | Rama base de desarrollo, integra features              |

Las features nuevas se ramifican desde `development` como `feature/<nombre>` y se integran de vuelta vía merge/PR.
