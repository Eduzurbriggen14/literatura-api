package com.example.Literatura.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;

public class ConsultaApi {

    public ConsultaApi() {
    }

    public String obtenerDatosApi(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();

        HttpResponse<String> response = null;

        try {
            // Enviar la solicitud HTTP
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta fue exitosa (código HTTP 200)
            if (response.statusCode() == 200) {
                String body = response.body();

                // Si la respuesta está vacía o no contiene los resultados, manejarlo
                if (body == null || body.isEmpty()) {
                    return "No se recibieron datos.";
                }

                // Devuelve la respuesta completa
                return body;
            } else if (response.statusCode() == 301) {
                // Si hay redirección, obtener la URL de la nueva ubicación
                HttpHeaders headers = response.headers();
                String location = headers.firstValue("Location").orElse("No se encontró la redirección");
                return "Redirigido a: " + location;
            } else {
                // Si la respuesta no fue exitosa, retornar un mensaje de error
                return "Error en la solicitud. Código de respuesta: " + response.statusCode();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Hubo un error al intentar hacer la solicitud.";
        }
    }
}
