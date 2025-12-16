package com.upt.pt.fx.service;

import java.net.URI;
import java.net.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    // ============================
    // POST
    // ============================
    public static JSONObject post(String path, JSONObject json) throws Exception {
        System.out.println("API POST: " + path); // Debug
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp); // Verifica se houve erro
        return new JSONObject(resp.body());
    }

    // ============================
    // GET (objeto)
    // ============================
    public static JSONObject getObject(String path) throws Exception {
        System.out.println("API GET OBJ: " + path); // Debug
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);
        return new JSONObject(resp.body());
    }

    // ============================
    // GET (lista) - ONDE O ERRO ESTAVA
    // ============================
    public static JSONArray getArray(String path) throws Exception {
        System.out.println("API GET LIST: " + path); // Debug
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .GET()
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        
        // 1. Verificar erros HTTP antes de converter
        tratarErro(resp);

        // 2. Se a resposta for vazia, devolve array vazio para não crashar
        if (resp.body() == null || resp.body().isBlank()) {
            return new JSONArray();
        }

        // 3. Tenta converter. Se o servidor devolver um Objeto em vez de lista, apanhamos aqui
        try {
            return new JSONArray(resp.body());
        } catch (Exception e) {
            System.err.println("ERRO JSON: O servidor não devolveu uma lista!");
            System.err.println("Resposta recebida: " + resp.body());
            throw e;
        }
    }

    // ============================
    // PUT & DELETE
    // ============================
    public static JSONObject put(String path, JSONObject json) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        tratarErro(resp);
        return new JSONObject(resp.body());
    }

    public static JSONObject delete(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .DELETE()
                .build();
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        // Em deletes, as vezes o body vem vazio, então cuidado com new JSONObject()
        if (resp.statusCode() >= 200 && resp.statusCode() < 300 && (resp.body() == null || resp.body().isBlank())) {
            return new JSONObject();
        }
        return new JSONObject(resp.body());
    }

    // ============================
    // MÉTODO AUXILIAR PARA VER ERROS
    // ============================
    private static void tratarErro(HttpResponse<String> resp) throws Exception {
        // Se o status for 400 (Bad Request) ou 500 (Erro Servidor)
        if (resp.statusCode() >= 300) {
            System.err.println("❌ ERRO API (" + resp.statusCode() + ")");
            System.err.println("❌ BODY: " + resp.body());
            throw new Exception("Erro API " + resp.statusCode() + ": " + resp.body());
        }
    }
}