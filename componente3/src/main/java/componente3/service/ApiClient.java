package componente3.service;

import java.net.URI;
import java.net.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    // ============================
    // POST com tratamento de erro
    // ============================
    public static JSONObject post(String path, JSONObject json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        // DEBUG: Mostrar a resposta
        System.out.println("POST " + path);
        System.out.println("Status: " + resp.statusCode());
        System.out.println("Response: " + resp.body());
        
        // Verificar se é JSON válido
        String body = resp.body().trim();
        if (body.isEmpty()) {
            return new JSONObject();
        }
        
        if (resp.statusCode() >= 400) {
            // Se for erro, criar um JSON de erro
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", true);
            errorJson.put("status", resp.statusCode());
            errorJson.put("message", body);
            return errorJson;
        }
        
        // Tentar parsear como JSON
        try {
            return new JSONObject(body);
        } catch (Exception e) {
            // Se não for JSON válido, retornar como mensagem
            JSONObject result = new JSONObject();
            result.put("message", body);
            return result;
        }
    }

    // ============================
    // GET (objeto) com tratamento de erro
    // ============================
    public static JSONObject getObject(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        // DEBUG: Mostrar a resposta
        System.out.println("GET " + path);
        System.out.println("Status: " + resp.statusCode());
        System.out.println("Response: " + resp.body());
        
        String body = resp.body().trim();
        if (body.isEmpty()) {
            return new JSONObject();
        }
        
        if (resp.statusCode() >= 400) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", true);
            errorJson.put("status", resp.statusCode());
            errorJson.put("message", body);
            return errorJson;
        }
        
        try {
            return new JSONObject(body);
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("message", body);
            return result;
        }
    }

    // ============================
    // GET (lista) com tratamento de erro
    // ============================
    public static JSONArray getArray(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        // DEBUG: Mostrar a resposta
        System.out.println("GET Array " + path);
        System.out.println("Status: " + resp.statusCode());
        System.out.println("Response: " + resp.body());
        
        String body = resp.body().trim();
        if (body.isEmpty()) {
            return new JSONArray();
        }
        
        if (resp.statusCode() >= 400) {
            // Retornar array vazio em caso de erro
            return new JSONArray();
        }
        
        try {
            return new JSONArray(body);
        } catch (Exception e) {
            // Se não for array, retornar array vazio
            return new JSONArray();
        }
    }

    // ============================
    // PUT com tratamento de erro
    // ============================
    public static JSONObject put(String path, JSONObject json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("PUT " + path);
        System.out.println("Status: " + resp.statusCode());
        System.out.println("Response: " + resp.body());
        
        String body = resp.body().trim();
        if (body.isEmpty()) {
            return new JSONObject();
        }
        
        if (resp.statusCode() >= 400) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", true);
            errorJson.put("status", resp.statusCode());
            errorJson.put("message", body);
            return errorJson;
        }
        
        try {
            return new JSONObject(body);
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("message", body);
            return result;
        }
    }

    // ============================
    // DELETE com tratamento de erro
    // ============================
    public static JSONObject delete(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("DELETE " + path);
        System.out.println("Status: " + resp.statusCode());
        System.out.println("Response: " + resp.body());
        
        String body = resp.body().trim();
        if (body.isEmpty()) {
            return new JSONObject();
        }
        
        if (resp.statusCode() >= 400) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", true);
            errorJson.put("status", resp.statusCode());
            errorJson.put("message", body);
            return errorJson;
        }
        
        try {
            return new JSONObject(body);
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("message", body);
            return result;
        }
    }
}