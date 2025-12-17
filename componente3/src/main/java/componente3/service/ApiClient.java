package componente3.service;

import java.net.URI;
import java.net.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    // ============================
    // POST (Criar / Login)
    // ============================
    public static JSONObject post(String path, JSONObject json) throws Exception {
        System.out.println("API POST: " + path); 
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);
        
        // Proteção contra resposta vazia
        if (resp.body() == null || resp.body().isBlank()) return new JSONObject();
        
        return new JSONObject(resp.body());
    }

    // ============================
    // GET ONE (Buscar 1 Objeto - ex: Empresa, Perfil)
    // ============================
    // NOTA: Renomeei de getObject para getJson para bater certo com o Controller
    public static JSONObject getJson(String path) throws Exception {
        System.out.println("API GET JSON: " + path);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);
        
        if (resp.body() == null || resp.body().isBlank()) return new JSONObject();
        
        return new JSONObject(resp.body());
    }

    // ============================
    // GET LIST (Buscar Array - ex: Lista de Propostas)
    // ============================
    public static JSONArray getArray(String path) throws Exception {
        System.out.println("API GET ARRAY: " + path);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);

        if (resp.body() == null || resp.body().isBlank()) {
            return new JSONArray();
        }

        try {
            return new JSONArray(resp.body());
        } catch (Exception e) {
            System.err.println("ERRO: O servidor não devolveu uma lista em " + path);
            throw e;
        }
    }

    // ============================
    // PUT (Atualizar)
    // ============================
    public static JSONObject put(String path, JSONObject json) throws Exception {
        System.out.println("API PUT: " + path);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);
        
        if (resp.body() == null || resp.body().isBlank()) return new JSONObject();
        
        return new JSONObject(resp.body());
    }

    // ============================
    // DELETE (Apagar)
    // ============================
    public static JSONObject delete(String path) throws Exception {
        System.out.println("API DELETE: " + path);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
        
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp); // Se der 404 ou 500, lança erro aqui
        
        if (resp.body() == null || resp.body().isBlank()) return new JSONObject();
        
        return new JSONObject(resp.body());
    }
    
    // ============================
    // AUXILIAR: TRATAMENTO DE ERROS
    // ============================
    private static void tratarErro(HttpResponse<String> resp) throws Exception {
        if (resp.statusCode() >= 300) {
            System.err.println("❌ ERRO API (" + resp.statusCode() + ")");
            System.err.println("❌ BODY: " + resp.body());
            throw new Exception("Erro API " + resp.statusCode() + ": " + resp.body());
        }
    }
}