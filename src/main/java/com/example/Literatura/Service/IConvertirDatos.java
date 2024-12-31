package com.example.Literatura.Service;

public interface IConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
