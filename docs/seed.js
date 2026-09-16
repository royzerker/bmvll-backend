// Datos de prueba para desarrollo local, consistentes entre las 7 colecciones.
// Requiere haber corrido antes init-mongo.js
// Uso: mongosh bmvll_db docs/seed.js

db = db.getSiblingDB("bmvll_db");

// ---------- usuarios ----------
const bibliotecario = db.usuarios.insertOne({
  tipoDocumento: "DNI",
  numeroDocumento: "45678912",
  nombres: "Luis",
  apellidos: "Ramírez",
  email: "luis.ramirez@bmvll.gob.pe",
  telefono: "912345678",
  rol: "BIBLIOTECARIO",
  estado: "ACTIVO",
  fechaRegistro: new Date()
});

const socio = db.usuarios.insertOne({
  tipoDocumento: "DNI",
  numeroDocumento: "70123456",
  nombres: "Ana",
  apellidos: "Torres",
  email: "ana.torres@example.com",
  telefono: "987654321",
  direccion: { direccion: "Av. Pastor Sevilla 123", distrito: "Villa El Salvador", referencia: "Frente al parque" },
  rol: "SOCIO",
  estado: "ACTIVO",
  fechaRegistro: new Date()
});

// ---------- categorias ----------
const categoria = db.categorias.insertOne({
  nombre: "Novela",
  descripcion: "Narrativa de ficción extensa",
  activo: true
});

// ---------- libros ----------
const libro = db.libros.insertOne({
  titulo: "Cien años de soledad",
  autores: ["Gabriel García Márquez"],
  isbn: "978-0307474728",
  categoriaId: categoria.insertedId,
  editorial: "Sudamericana",
  anioPublicacion: 1967,
  idioma: "Español",
  sinopsis: "La historia de la familia Buendía en Macondo.",
  activo: true,
  fechaRegistro: new Date()
});

// ---------- ejemplares (3 copias del mismo libro) ----------
const ejemplar1 = db.ejemplares.insertOne({
  libroId: libro.insertedId,
  codigoInventario: "BMVLL-000001",
  estado: "PRESTADO",
  condicion: "BUENO",
  ubicacion: { sede: "Sede Central VES", estante: "A-12" },
  fechaAdquisicion: new Date()
});

db.ejemplares.insertMany([
  {
    libroId: libro.insertedId,
    codigoInventario: "BMVLL-000002",
    estado: "DISPONIBLE",
    condicion: "NUEVO",
    ubicacion: { sede: "Sede Central VES", estante: "A-12" },
    fechaAdquisicion: new Date()
  },
  {
    libroId: libro.insertedId,
    codigoInventario: "BMVLL-000003",
    estado: "DISPONIBLE",
    condicion: "BUENO",
    ubicacion: { sede: "Sede Central VES", estante: "A-12" },
    fechaAdquisicion: new Date()
  }
]);

// ---------- prestamos (usa el ejemplar marcado PRESTADO arriba) ----------
db.prestamos.insertOne({
  ejemplarId: ejemplar1.insertedId,
  libroId: libro.insertedId,
  usuarioId: socio.insertedId,
  bibliotecarioId: bibliotecario.insertedId,
  fechaPrestamo: new Date(),
  fechaDevolucionEsperada: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
  fechaDevolucionReal: null,
  renovaciones: [],
  estado: "ACTIVO",
  observaciones: ""
});

print("Datos de prueba insertados: 2 usuarios (1 bibliotecario, 1 socio), 1 categoria, 1 libro, 3 ejemplares (1 prestado), 1 prestamo activo.");
