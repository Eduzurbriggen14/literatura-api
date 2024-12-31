package com.example.Literatura.Models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record ResultadoBusqueda(

        @JsonAlias("results") List<DatosLibro> resultados

) {
}