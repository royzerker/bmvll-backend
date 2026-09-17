# bmvll-backend

API REST del sistema de gestión de préstamos de la **Biblioteca Municipal Mario Vargas Llosa** (Villa El Salvador).

Backend construido con **Spring Boot** + **MongoDB**, expone los endpoints que consumirá el frontend en **React**.

## Estado actual (setup inicial)

Este repositorio contiene únicamente el **scaffold base** del backend:

- Proyecto Spring Boot generado con Spring Initializr (Maven).
- Dependencias: `spring-boot-starter-webmvc`, `spring-boot-starter-data-mongodb`, `spring-boot-starter-validation`, `lombok`, `devtools`.
- Conexión a MongoDB configurada en `src/main/resources/application.properties`.

Modelamiento de datos ya definido en [`docs/MODELADO.md`](./docs/MODELADO.md) (7 colecciones, nombres y campos en inglés: `users`, `categories`, `books`, `copies`, `loans`, `fines`, `reservations`).

Pendiente (ver [`docs/PLANIFICACION.md`](./docs/PLANIFICACION.md) y [`TODO.md`](./TODO.md)):

- Entidades Java, repositorios y servicios sobre el modelo ya definido.
- Endpoints de catálogo, préstamo/devolución e historial.
- Integración con el frontend en React.

## Requisitos

- Java 17+
- Maven (o usar el wrapper `./mvnw`)
- MongoDB corriendo en `localhost:27017` (o ajustar `spring.data.mongodb.uri`)

## Ejecutar en local

```bash
./mvnw spring-boot:run
```

## Flujo de ramas

El proyecto usa 3 ramas principales:

| Rama          | Propósito                                              |
|---------------|---------------------------------------------------------|
| `master`      | Versión estable / release                              |
| `staging`     | Integración pre-producción, pruebas antes de `master`  |
| `development` | Rama base de desarrollo, integra features              |

Las features nuevas se ramifican desde `development` como `feature/<nombre>` y se integran de vuelta vía merge/PR.
