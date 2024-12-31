package com.example.Literatura.Repository;

import com.example.Literatura.Models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoresRepository extends JpaRepository<Autor, Integer> {

   /* @Query("SELECT DISTINCT a FROM Autores a JOIN a.libros l")
    List<Autores> findDistinctAutoresByLibros();

    @Query("SELECT a FROM Autores a WHERE a.anioMuerte IS NULL OR a.anioMuerte > :anio")
    List<Autores> findAutoresVivosPorAnio(@Param("anio") int anio);*/

    Autor findByNombreIgnoreCase(String nombre);
    List<Autor> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(int anioInicial, int anioFinal);

}
