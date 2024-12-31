# Sistema de Gestion de Libros

Este proyecto permite la gestion de libros y autores mediante la consulta a una API externa

[Gutendex](https://gutendex.com)

## Funcionalidades del sistema

Funcionalidades del Sistema

Menú Principal

Desde el menú, se pueden realizar las siguientes operaciones:

Buscar libro por título: Permite buscar en la base de datos y, si no está registrado, buscar y registrar el libro desde la API externa.

Listar libros registrados: Muestra todos los libros almacenados en la base de datos.

Listar autores registrados: Presenta los autores y los libros asociados en la base de datos.

Listar autores vivos en un año específico: Permite consultar autores vivos en un año específico.

Listar libros por idioma: Filtra los libros en la base de datos según el idioma especificado.

Salir: Termina la aplicación.

### Ejemplo del menu en consola de Intellij

```
Ingrese el numero de la operacion a realizar

1-Buscar Libros por Titulo (que seran guardados en la BBDD MySQL)

2-Listar Libros registrados en la BBDD

3-Listar Autores registrados

4-Listar Autores vivos en un determinado anio

5-Listar Libros por Idioma

0-PARA SALIR

```

## Configuracion Inicial del entorno Java Y Spring

```
Configuración al crear el proyecto en Spring Initializr:

Java (versión 17 en adelante)

Maven (Initializr utiliza la versión 4)

Spring Boot (ultima version al 28/12/24)

Proyecto en JAR

Dependencias para agregar al crear el proyecto en Spring Initializr:

Spring Data JPA

MySQL Driver --Connector Java Version 8.0.33

Jackson databind

```
