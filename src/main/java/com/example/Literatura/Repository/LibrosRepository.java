package com.example.Literatura.Repository;

import com.example.Literatura.Models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibrosRepository extends JpaRepository<Libro, Integer> {


    Libro findByTitulo (String titulo);

    List<Libro> findByIdiomaContaining(String idiomas);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.idioma")
    List<Libro> findAllWithIdiomas();
}