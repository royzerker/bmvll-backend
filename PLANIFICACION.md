# Planificación — bmvll-backend

## 1. Problema o necesidad

La Biblioteca Municipal Mario Vargas Llosa (Villa El Salvador) gestiona actualmente el préstamo y la devolución de libros de forma manual, mediante un cuaderno físico sin un campo fijo para la fecha de devolución. Esto genera:

- Pérdida de anotaciones por hojas sueltas o tachones.
- Demoras de varios minutos para confirmar si un usuario tiene un libro pendiente.
- Falta de visibilidad inmediata sobre qué títulos están disponibles.

Una encuesta aplicada a 42 visitantes lo confirma: el **57%** no pudo llevarse un libro por no saber si estaba disponible, y la claridad percibida sobre la fecha de devolución fue de apenas **2.3/5**.

## 2. Objetivo del proyecto

Desarrollar un sistema de gestión de préstamos que permita a la biblioteca registrar, controlar y dar seguimiento digital a los libros prestados, reduciendo la pérdida de material y mejorando el tiempo de atención al usuario.

## 3. Stack técnico

| Capa      | Tecnología                                   |
|-----------|-----------------------------------------------|
| Backend   | Spring Boot (Java), Spring Web, Spring Data MongoDB, Bean Validation |
| Base de datos | MongoDB (colecciones: `libros`, `prestamos`, `usuarios`) |
| Frontend  | React (repositorio/etapa separada)            |

> Cambio respecto al planteamiento inicial (Go + Fiber): se migra a **Spring Boot + MongoDB** para el backend. El frontend en React se mantiene sin cambios.

## 4. Alcance de este avance (setup inicial)

- [x] Definición de nombre de proyecto/repositorio: `bmvll-backend`.
- [x] Scaffold del proyecto Spring Boot (Maven) con dependencias base: Web, Data MongoDB, Validation, Lombok, DevTools.
- [x] Configuración de conexión a MongoDB (`application.properties`).
- [x] Repositorio Git inicializado con 3 ramas: `master`, `staging`, `development`.
- [ ] Modelamiento de entidades y colecciones (pendiente — ver sección 5).
- [ ] Endpoints REST de catálogo, préstamo/devolución e historial.

## 5. Modelamiento de datos (pendiente de definir)

Borrador inicial de entidades a modelar en la siguiente iteración:

- **Libro**: id, título, autor, ISBN, cantidad total, cantidad disponible.
- **Usuario**: id, nombre, DNI/código de socio, contacto.
- **Préstamo**: id, referencia a libro, referencia a usuario, fecha de préstamo, fecha de devolución esperada, fecha de devolución real, estado (activo/devuelto/atrasado).

Esto se formalizará (con relaciones/embedding en MongoDB) antes de escribir los repositorios y controladores.

## 6. Flujo de ramas (Git)

Se manejan 3 ramas principales:

- **`master`** — versión estable/release.
- **`staging`** — integración pre-producción, validación antes de pasar a `master`.
- **`development`** — rama de trabajo donde se integran las features.

Las nuevas funcionalidades se crean como `feature/<nombre>` desde `development`, y se integran de vuelta por merge (o Pull Request desde APF2 en adelante).

## 7. Próximos pasos

1. Definir el modelamiento final de entidades y colecciones MongoDB.
2. Implementar catálogo de libros con disponibilidad (GET `/api/libros`).
3. Implementar registro de préstamo y devolución.
4. Implementar historial de préstamos por usuario (APF2).
5. Incorporar Pull Requests, una Release identificable y tablero de gestión de tareas (APF2).
