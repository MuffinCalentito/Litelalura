package com.literatura.litelaluraChallenge.Principal;
import com.literatura.litelaluraChallenge.config.ConsumoAPI;
import com.literatura.litelaluraChallenge.config.Convertir;
import com.literatura.litelaluraChallenge.models.Autor;
import com.literatura.litelaluraChallenge.models.Libro;
import com.literatura.litelaluraChallenge.models.Resultados;
import com.literatura.litelaluraChallenge.models.DatosLibro;
import com.literatura.litelaluraChallenge.repository.IAutorRepository;
import com.literatura.litelaluraChallenge.repository.IBookRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private Convertir convertir = new Convertir();
    private static String URL_BASE = "https://gutendex.com/books/?search=";
    //var json = consumoApi.obtenerDatos("https://gutendex.com/books/?ids=1513");
    //var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=Romeo%20and%20Juliet");
    private List<Libro> datosLibro = new ArrayList<>();
    private IBookRepository libroRepository;
    private IAutorRepository autorRepository;
    public Principal(IBookRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    ****************************************************
                                Biblioteca de Libros: Gutendex
                    ****************************************************
                    
                    1 - Agregar Libro a la base de datos
                    2 - Buscar libro por titulo
                    3 - Libros buscados
                    4 - Buscar todos los Autores de libros buscados
                    5 - Buscar Autores por año
                    6 - Buscar Libros por Idioma
                    0 - Salir
                    
                    ***************************************************
                                     Ingrese una opción
                    ***************************************************
                    
                    """;
            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {

                System.out.println("|****************************************|");
                System.out.println("|  Por favor, ingrese un número válido.  |");
                System.out.println("|****************************************|\n");
                teclado.nextLine();
                continue;
            }
            switch (opcion){
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    buscarLibroPorNombre();
                    break;
                case 3:
                    librosBuscados();
                    break;
                case 4:
                    BuscarAutores();
                    break;
                case 5:
                    buscarAutoresPorYear();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación ...");
                    break;
                default:
                    System.out.println("|****************************************|");
                    System.out.println("La opción indicada no es valida, igresea otra opción\n");
            }
        }
    }

    private Libro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = teclado.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        //System.out.println("JSON INICIAL: " + json);
        Resultados datos = convertir.convertirDatosJsonAJava(json, Resultados.class);

            if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
                DatosLibro primerLibro = datos.getResultadoLibros().get(0); // Obtener el primer libro de la lista
                return new Libro(primerLibro);
            } else {
                System.out.println("No se encontraron resultados.");
                return null;
            }
    }


    private void buscarLibroWeb() {
        Libro libro = getDatosLibro();

        if (libro == null){
            System.out.println("Libro no encontrado en la base de Gutendex");
            return;
        }

        try{
            boolean libroExiste = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroExiste){
                System.out.println("El libro ya se registró en la base de datos");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("El libro ya ha sido registrado!");
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se ha registrado ningún libro en la base de datos aún.");
        } else {
            System.out.println("Los libros que han sido encontrados en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Ingrese el nombre del libro a buscar:");
        var nombre = teclado.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(nombre);
        if (libroBuscado != null) {
            System.out.println("El libro buscado fue: " + libroBuscado);
        } else {
            System.out.println("El libro '" + nombre + "' no se encontró.");
        }
    }

    private void BuscarAutores(){
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No se encontro el libro en la base de datos. \n");
        } else {
            System.out.println("Los libros encontrados en la base de datos son: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }

    private void  buscarLibrosPorIdioma(){
        System.out.println("Ingrese el idioma en el que quiere buscar los libros: \n");
        System.out.println("|***********************************|");
        System.out.println("  es : Español ");
        System.out.println("  en : Ingles  ");
        System.out.println("|***********************************|\n");

        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontró libros con el idioma especificado en la base de datos");
        } else {
            System.out.println("Libros encontrado en el idioma especificado:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarAutoresPorYear() {
        System.out.println("Indica el año para presentar autores que estuvieron con vida: \n");
        var year = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresVivos = autorRepository.findByFechaNacimientoLessThanOrFechaFallecimientoGreaterThanEqual(year, year);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + year);
        } else {
            System.out.println("Los autores que estaban vivos en el año " + year + " son:");
            Set<String> autoresRevisados = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getFechaNacimiento() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getFechaNacimiento() <= year && autor.getFechaFallecimiento() >= year) {
                        if (autoresRevisados.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

}
