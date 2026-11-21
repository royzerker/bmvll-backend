package com.bmvll.backend.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bmvll.backend.model.Book;
import com.bmvll.backend.model.Category;
import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyCondition;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.model.Location;
import com.bmvll.backend.repository.BookRepository;
import com.bmvll.backend.repository.CategoryRepository;
import com.bmvll.backend.repository.CopyRepository;

/**
 * Carga el catálogo público (categorías, libros y ejemplares) para tener datos de prueba en una base nueva.
 * Se activa con SEED_ENABLED=true y es idempotente: solo agrega lo que falta (categoría por nombre,
 * libro por ISBN o título), así que se puede dejar activo en cada arranque.
 * No crea socios ni ningún dato personal.
 *
 * Los ISBN son sintéticos (prefijo 978-612-999, con dígito de control válido): sirven como clave única
 * del seed, no corresponden a ediciones reales.
 */
@Component
@Profile("!test")
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class CatalogSeedRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogSeedRunner.class);
    private static final String BRANCH = "Sede Central - Villa El Salvador";
    private static final Instant ACQUIRED = LocalDate.of(2026, 3, 2).atStartOfDay().toInstant(ZoneOffset.UTC);

    private record SeedCategory(String code, String name, String description) {}

    private record SeedBook(String category, String title, String author, String publisher, int year,
            String synopsis, int copies) {}

    private static final List<SeedCategory> CATEGORIES = List.of(
            new SeedCategory("LP", "Literatura peruana", "Narrativa y ensayo de autores peruanos."),
            new SeedCategory("LL", "Literatura latinoamericana", "Obras representativas de América Latina."),
            new SeedCategory("CU", "Clásicos universales", "Grandes obras de la literatura mundial."),
            new SeedCategory("PO", "Poesía", "Poesía peruana y universal."),
            new SeedCategory("IJ", "Infantil y juvenil", "Lecturas para niños y adolescentes."),
            new SeedCategory("HP", "Historia del Perú", "Historia, cultura e identidad peruana."),
            new SeedCategory("CD", "Ciencia y divulgación", "Ciencia explicada para el público general."));

    private static final List<SeedBook> BOOKS = List.of(
            new SeedBook("LP", "La ciudad y los perros", "Mario Vargas Llosa", "Seix Barral", 1963,
                    "Novela ambientada en el colegio militar Leoncio Prado.", 3),
            new SeedBook("LP", "Conversación en La Catedral", "Mario Vargas Llosa", "Seix Barral", 1969,
                    "Zavalita y Ambrosio repasan la Lima de la dictadura de Odría.", 2),
            new SeedBook("LP", "La fiesta del Chivo", "Mario Vargas Llosa", "Alfaguara", 2000,
                    "Los últimos días de la dictadura de Trujillo en República Dominicana.", 2),
            new SeedBook("LP", "Pantaleón y las visitadoras", "Mario Vargas Llosa", "Seix Barral", 1973,
                    "Un capitán del ejército organiza un insólito servicio en la Amazonía.", 2),
            new SeedBook("LP", "Los ríos profundos", "José María Arguedas", "Losada", 1958,
                    "Ernesto, entre el mundo andino y el colegio religioso de Abancay.", 3),
            new SeedBook("LP", "El zorro de arriba y el zorro de abajo", "José María Arguedas", "Losada", 1971,
                    "Chimbote y la transformación del Perú moderno.", 1),
            new SeedBook("LP", "Un mundo para Julius", "Alfredo Bryce Echenique", "Barral", 1970,
                    "La infancia de un niño de la alta sociedad limeña.", 2),
            new SeedBook("LP", "El mundo es ancho y ajeno", "Ciro Alegría", "Ercilla", 1941,
                    "La lucha de la comunidad de Rumi por sus tierras.", 2),
            new SeedBook("LP", "Lima la horrible", "Sebastián Salazar Bondy", "Era", 1964,
                    "Ensayo crítico sobre el mito de la Lima colonial.", 1),
            new SeedBook("LL", "Cien años de soledad", "Gabriel García Márquez", "Sudamericana", 1967,
                    "Siete generaciones de la familia Buendía en Macondo.", 3),
            new SeedBook("LL", "Pedro Páramo", "Juan Rulfo", "Fondo de Cultura Económica", 1955,
                    "Juan Preciado busca a su padre en el pueblo fantasma de Comala.", 2),
            new SeedBook("LL", "Rayuela", "Julio Cortázar", "Sudamericana", 1963,
                    "Novela que puede leerse en más de un orden.", 1),
            new SeedBook("LL", "Ficciones", "Jorge Luis Borges", "Sur", 1944,
                    "Cuentos sobre laberintos, bibliotecas infinitas y espejos.", 2),
            new SeedBook("LL", "La casa de los espíritus", "Isabel Allende", "Plaza & Janés", 1982,
                    "La saga de la familia Trueba a lo largo del siglo XX.", 2),
            new SeedBook("CU", "Don Quijote de la Mancha", "Miguel de Cervantes", "Real Academia Española", 1605,
                    "Las aventuras del hidalgo que quiso ser caballero andante.", 3),
            new SeedBook("CU", "Crimen y castigo", "Fiódor Dostoyevski", "Alianza", 1866,
                    "Raskólnikov y las consecuencias de un asesinato.", 1),
            new SeedBook("CU", "Orgullo y prejuicio", "Jane Austen", "Penguin Clásicos", 1813,
                    "Elizabeth Bennet y el señor Darcy en la Inglaterra rural.", 2),
            new SeedBook("CU", "1984", "George Orwell", "Debolsillo", 1949,
                    "Una sociedad vigilada por el Gran Hermano.", 2),
            new SeedBook("PO", "Los heraldos negros", "César Vallejo", "Souza Ferreyra", 1919,
                    "Primer poemario de Vallejo.", 2),
            new SeedBook("PO", "Trilce", "César Vallejo", "Talleres de la Penitenciaría", 1922,
                    "Obra cumbre de la vanguardia poética en español.", 1),
            new SeedBook("PO", "Veinte poemas de amor y una canción desesperada", "Pablo Neruda", "Nascimento", 1924,
                    "Poemario de amor más leído en lengua española.", 2),
            new SeedBook("PO", "Poemas humanos", "César Vallejo", "Les Éditions des Presses Modernes", 1939,
                    "Poemas publicados póstumamente en París.", 1),
            new SeedBook("IJ", "El principito", "Antoine de Saint-Exupéry", "Salamandra", 1943,
                    "Un aviador conoce a un pequeño príncipe venido de otro planeta.", 3),
            new SeedBook("IJ", "Paco Yunque", "César Vallejo", "Mosca Azul", 1951,
                    "Un niño humilde enfrenta la injusticia en su primer día de escuela.", 3),
            new SeedBook("IJ", "Cuentos de la selva", "Horacio Quiroga", "Losada", 1918,
                    "Relatos de animales de la selva misionera.", 2),
            new SeedBook("IJ", "Harry Potter y la piedra filosofal", "J. K. Rowling", "Salamandra", 1997,
                    "Un niño descubre que es mago en su undécimo cumpleaños.", 2),
            new SeedBook("HP", "Tradiciones peruanas", "Ricardo Palma", "Peisa", 1872,
                    "Relatos breves sobre la historia y las costumbres del Perú.", 2),
            new SeedBook("HP", "Comentarios reales de los incas", "Inca Garcilaso de la Vega", "Fondo de Cultura Económica", 1609,
                    "Historia y cultura del Tahuantinsuyo contada por un mestizo cusqueño.", 2),
            new SeedBook("HP", "Siete ensayos de interpretación de la realidad peruana", "José Carlos Mariátegui", "Amauta", 1928,
                    "Análisis de la economía, la educación y el problema de la tierra.", 2),
            new SeedBook("CD", "Breve historia del tiempo", "Stephen Hawking", "Crítica", 1988,
                    "Del Big Bang a los agujeros negros, explicado sin fórmulas.", 2),
            new SeedBook("CD", "Cosmos", "Carl Sagan", "Planeta", 1980,
                    "Un recorrido por el universo y la historia de la ciencia.", 2),
            new SeedBook("CD", "Sapiens: de animales a dioses", "Yuval Noah Harari", "Debate", 2011,
                    "Breve historia de la humanidad.", 2));

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CopyRepository copyRepository;

    public CatalogSeedRunner(CategoryRepository categoryRepository, BookRepository bookRepository,
            CopyRepository copyRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.copyRepository = copyRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        int newCategories = 0;
        int newBooks = 0;
        int newCopies = 0;

        Map<String, String> categoryIds = new HashMap<>();
        for (SeedCategory c : CATEGORIES) {
            Category category = categoryRepository.findFirstByNameIgnoreCase(c.name()).orElse(null);
            if (category == null) {
                category = categoryRepository.save(new Category(null, c.name(), c.description(), true));
                newCategories++;
            }
            categoryIds.put(c.code(), category.getId());
        }

        for (int i = 0; i < BOOKS.size(); i++) {
            SeedBook b = BOOKS.get(i);
            int number = i + 1;
            String isbn = syntheticIsbn(number);
            if (bookRepository.existsByIsbn(isbn) || bookRepository.existsByTitleIgnoreCase(b.title())) {
                continue;
            }

            Instant now = Instant.now();
            Book book = bookRepository.save(Book.builder()
                    .title(b.title())
                    .authors(List.of(b.author()))
                    .isbn(isbn)
                    .categoryId(categoryIds.get(b.category()))
                    .publisher(b.publisher())
                    .publicationYear(b.year())
                    .language("es")
                    .synopsis(b.synopsis())
                    .active(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build());
            newBooks++;

            for (int n = 1; n <= b.copies(); n++) {
                String code = String.format("BMVLL-%03d-%02d", number, n);
                if (copyRepository.existsByInventoryCode(code)) {
                    continue;
                }
                copyRepository.save(Copy.builder()
                        .bookId(book.getId())
                        .inventoryCode(code)
                        .status(CopyStatus.AVAILABLE)
                        .condition(n == 1 ? CopyCondition.GOOD : CopyCondition.NEW)
                        .location(new Location(BRANCH, String.format("%s-%02d", b.category(), (number % 5) + 1)))
                        .acquisitionDate(ACQUIRED)
                        .build());
                newCopies++;
            }
        }

        log.info("Seed del catálogo: {} categorías, {} libros y {} ejemplares nuevos", newCategories, newBooks, newCopies);
    }

    /** 978-612-999-NNN-C con dígito de control ISBN-13 válido. */
    static String syntheticIsbn(int number) {
        String digits = String.format("978612999%03d", number);
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        int check = (10 - sum % 10) % 10;
        return String.format("978-612-999-%03d-%d", number, check);
    }
}
