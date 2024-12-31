package com.example.Literatura.Principal;

import com.example.Literatura.Models.*;
import com.example.Literatura.Repository.AutoresRepository;
import com.example.Literatura.Repository.LibrosRepository;
import com.example.Literatura.Service.ConsultaApi;
import com.example.Literatura.Service.ConvertirDatos;

import java.util.*;

public class Principal {
    private Scanner scan = new Scanner(System.in);
    private LibrosRepository librosRepository;
    private AutoresRepository autoresRepository;
    private ConsultaApi consulta = new ConsultaApi();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvertirDatos convertir = new ConvertirDatos();

    public Principal(LibrosRepository librosRepository, AutoresRepository autoresRepository) {
        this.librosRepository = librosRepository;
        this.autoresRepository = autoresRepository;
    }

    public void mostrarMenu() {
        buscar();
        int opcion;
        do {
            // Muestra el menú
            System.out.println("Ingrese el numero de la operacion a realizar\n" +
                    "1-Buscar Libros por Titulo\n" +
                    "2-Listar Libros registrados en la BBDD\n" +
                    "3-Listar Autores registrados\n" +
                    "4-Listar Autores vivos en un determinado anio\n" +
                    "5-Listar Libros por Idioma\n" +
                    "0-PARA SALIR\n");

            opcion = scan.nextInt();
            scan.nextLine();

            while (opcion < 0 || opcion > 5) {
                System.out.println("Ingrese nuevamente el numero de la operacion a realizar\n" +
                        "1-Buscar Libros por Titulo\n" +
                        "2-Listar Libros registrados en la BBDD\n" +
                        "3-Listar Autores registrados\n" +
                        "4-Listar Autores vivos en un determinado anio\n" +
                        "5-Listar Libros por Idioma\n" +
                        "0-PARA SALIR\n");
                opcion = scan.nextInt();
                scan.nextLine();
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresPorAño();
                    break;
                case 5:
                    listarLibrosPorIdiomas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

        } while (opcion != 0);
    }

    private ResultadoBusqueda getDatosLibros() {
        var tituloLibro = scan.nextLine();
        var json = consulta.obtenerDatosApi(URL_BASE + tituloLibro.replace(" ", "%20"));
        ResultadoBusqueda datosLibros = convertir.obtenerDatos(json, ResultadoBusqueda.class);
        return datosLibros;
    }

    private Libro crearLibro(DatosLibro datosLibros, Autor autor) {
        if (autor != null) {
            return new Libro(datosLibros, autor);
        } else {
            System.out.println("Error al obtener los datos del libro");
            return null;
        }
    }
    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro a buscar");
        ResultadoBusqueda datos = getDatosLibros();
        if (!datos.resultados().isEmpty()) {
            DatosLibro datosLibros = datos.resultados().get(0);
            DatosAutor datosAutores = datosLibros.autor().get(0);

            Libro libroEncontrado = librosRepository.findByTitulo(datosLibros.titulo());
            if (libroEncontrado != null) {
                System.out.println("Libro ya registrado");
                System.out.println(libroEncontrado.toString());
            } else {
                Autor autorEncontrado = autoresRepository.findByNombreIgnoreCase(datosAutores.nombreAutor());
                if (autorEncontrado != null) {
                    Libro nuevoLibro = crearLibro(datosLibros, autorEncontrado);
                    librosRepository.save(nuevoLibro);
                    System.out.println("Libro registrado en la bbdd\n" + nuevoLibro);
                    System.out.println("************************\n");
                } else {
                    Autor nuevoAutor = new Autor(datosAutores);
                    nuevoAutor = autoresRepository.save(nuevoAutor);
                    Libro nuevoLibro = crearLibro(datosLibros, nuevoAutor);
                    librosRepository.save(nuevoLibro);
                    System.out.println("**** Libro ****\n" + nuevoLibro + "\n");
                    System.out.println("************************\n");
                }
            }
        } else {
            System.out.println("Libro No Encontrado, intenta de nuevo");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = librosRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        System.out.println("***** Libros en la BBDD: *****\n");
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println("***** LIBRO " + libro.getId() + "*****");
                    System.out.println(libro);
                    System.out.println("************************\n");
                });
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autoresRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
            return;
        }
        System.out.println("*****Autores registrados:*****\n");
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarAutoresPorAño() {
        System.out.println("Escribe el año en el que deseas buscar: ");
        var anio = scan.nextInt();
        scan.nextLine();
        if(anio < 0) {
            System.out.println("El año debe ser mayor a cero");
            return;
        }
        List<Autor> autoresPorAnio = autoresRepository.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio, anio);
        if (autoresPorAnio.isEmpty()) {
            System.out.println("No se encontraron autores en ese año");
            return;
        }
        System.out.println("-----Autores vivos en el " + anio + " SON: -----\n");
        autoresPorAnio.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdiomas() {
        System.out.println("Escribe el idioma por el que deseas buscar: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """;
        System.out.println(menu);
        var idioma = scan.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo");
            return;
        }
        List<Libro> librosPorIdioma = librosRepository.findByIdiomaContaining(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No encontramos libros con el idioma : " + idioma);
            return;
        }
        System.out.println("----- Libros en el idioma ("+idioma+") registrados en la bbdd SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }


    public void buscar(){
        String url = "https://gutendex.com/books?search=dickens%20great";
        var json = consulta.obtenerDatosApi(url);
        System.out.println(json);
    }



}
