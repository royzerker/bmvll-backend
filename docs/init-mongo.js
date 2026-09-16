// Crea las colecciones del sistema de prestamos con validacion y sus indices.
// Uso: mongosh bmvll_db docs/init-mongo.js

db = db.getSiblingDB("bmvll_db");

// ---------- Coleccion: usuarios ----------
db.createCollection("usuarios", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["tipoDocumento", "numeroDocumento", "nombres", "apellidos", "email", "rol", "estado", "fechaRegistro"],
      properties: {
        tipoDocumento: { enum: ["DNI", "CE", "PASAPORTE"] },
        numeroDocumento: { bsonType: "string" },
        nombres: { bsonType: "string" },
        apellidos: { bsonType: "string" },
        email: { bsonType: "string" },
        telefono: { bsonType: "string" },
        direccion: {
          bsonType: "object",
          properties: {
            direccion: { bsonType: "string" },
            distrito: { bsonType: "string" },
            referencia: { bsonType: "string" }
          }
        },
        rol: { enum: ["ADMIN", "BIBLIOTECARIO", "SOCIO"] },
        estado: { enum: ["ACTIVO", "SUSPENDIDO", "INACTIVO"] },
        passwordHash: { bsonType: "string" },
        fechaRegistro: { bsonType: "date" },
        fechaActualizacion: { bsonType: "date" }
      }
    }
  },
  validationLevel: "moderate"
});
db.usuarios.createIndex({ tipoDocumento: 1, numeroDocumento: 1 }, { unique: true });
db.usuarios.createIndex({ email: 1 }, { unique: true });
db.usuarios.createIndex({ rol: 1 });

// ---------- Coleccion: categorias ----------
db.createCollection("categorias", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["nombre", "activo"],
      properties: {
        nombre: { bsonType: "string" },
        descripcion: { bsonType: "string" },
        activo: { bsonType: "bool" }
      }
    }
  },
  validationLevel: "moderate"
});
db.categorias.createIndex({ nombre: 1 }, { unique: true });

// ---------- Coleccion: libros (catalogo, sin conteo de ejemplares) ----------
db.createCollection("libros", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["titulo", "autores", "isbn", "categoriaId", "activo", "fechaRegistro"],
      properties: {
        titulo: { bsonType: "string" },
        autores: { bsonType: "array", items: { bsonType: "string" } },
        isbn: { bsonType: "string" },
        categoriaId: { bsonType: "objectId" },
        editorial: { bsonType: "string" },
        anioPublicacion: { bsonType: "int" },
        idioma: { bsonType: "string" },
        sinopsis: { bsonType: "string" },
        activo: { bsonType: "bool" },
        fechaRegistro: { bsonType: "date" },
        fechaActualizacion: { bsonType: "date" }
      }
    }
  },
  validationLevel: "moderate"
});
db.libros.createIndex({ isbn: 1 }, { unique: true });
db.libros.createIndex({ titulo: "text", autores: "text" });
db.libros.createIndex({ categoriaId: 1 });

// ---------- Coleccion: ejemplares (inventario fisico, 1 doc por copia) ----------
db.createCollection("ejemplares", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["libroId", "codigoInventario", "estado"],
      properties: {
        libroId: { bsonType: "objectId" },
        codigoInventario: { bsonType: "string" },
        estado: { enum: ["DISPONIBLE", "PRESTADO", "RESERVADO", "EN_REPARACION", "PERDIDO", "DE_BAJA"] },
        condicion: { enum: ["NUEVO", "BUENO", "REGULAR", "DETERIORADO"] },
        ubicacion: {
          bsonType: "object",
          properties: {
            sede: { bsonType: "string" },
            estante: { bsonType: "string" }
          }
        },
        fechaAdquisicion: { bsonType: "date" }
      }
    }
  },
  validationLevel: "moderate"
});
db.ejemplares.createIndex({ codigoInventario: 1 }, { unique: true });
db.ejemplares.createIndex({ libroId: 1 });
db.ejemplares.createIndex({ libroId: 1, estado: 1 });
db.ejemplares.createIndex({ estado: 1 });

// ---------- Coleccion: prestamos ----------
db.createCollection("prestamos", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["ejemplarId", "libroId", "usuarioId", "bibliotecarioId", "fechaPrestamo", "fechaDevolucionEsperada", "estado"],
      properties: {
        ejemplarId: { bsonType: "objectId" },
        libroId: { bsonType: "objectId" },
        usuarioId: { bsonType: "objectId" },
        bibliotecarioId: { bsonType: "objectId" },
        fechaPrestamo: { bsonType: "date" },
        fechaDevolucionEsperada: { bsonType: "date" },
        fechaDevolucionReal: { bsonType: ["date", "null"] },
        renovaciones: {
          bsonType: "array",
          items: {
            bsonType: "object",
            properties: {
              fecha: { bsonType: "date" },
              nuevaFechaDevolucionEsperada: { bsonType: "date" }
            }
          }
        },
        estado: { enum: ["ACTIVO", "DEVUELTO", "ATRASADO", "PERDIDO"] },
        observaciones: { bsonType: "string" }
      }
    }
  },
  validationLevel: "moderate"
});
db.prestamos.createIndex({ usuarioId: 1 });
db.prestamos.createIndex({ ejemplarId: 1 });
db.prestamos.createIndex({ estado: 1 });
db.prestamos.createIndex({ usuarioId: 1, estado: 1 });

// ---------- Coleccion: multas ----------
db.createCollection("multas", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["prestamoId", "usuarioId", "monto", "motivo", "estado", "fechaGeneracion"],
      properties: {
        prestamoId: { bsonType: "objectId" },
        usuarioId: { bsonType: "objectId" },
        monto: { bsonType: ["double", "decimal"] },
        motivo: { enum: ["ATRASO", "PERDIDA", "DANIO"] },
        estado: { enum: ["PENDIENTE", "PAGADO", "CONDONADO"] },
        fechaGeneracion: { bsonType: "date" },
        fechaPago: { bsonType: "date" }
      }
    }
  },
  validationLevel: "moderate"
});
db.multas.createIndex({ usuarioId: 1 });
db.multas.createIndex({ usuarioId: 1, estado: 1 });
db.multas.createIndex({ prestamoId: 1 });

// ---------- Coleccion: reservas ----------
db.createCollection("reservas", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["libroId", "usuarioId", "fechaReserva", "fechaExpiracion", "estado"],
      properties: {
        libroId: { bsonType: "objectId" },
        usuarioId: { bsonType: "objectId" },
        fechaReserva: { bsonType: "date" },
        fechaExpiracion: { bsonType: "date" },
        estado: { enum: ["PENDIENTE", "NOTIFICADA", "CONVERTIDA", "CANCELADA", "EXPIRADA"] }
      }
    }
  },
  validationLevel: "moderate"
});
db.reservas.createIndex({ libroId: 1, estado: 1 });
db.reservas.createIndex({ usuarioId: 1 });

print("Colecciones creadas: usuarios, categorias, libros, ejemplares, prestamos, multas, reservas");
