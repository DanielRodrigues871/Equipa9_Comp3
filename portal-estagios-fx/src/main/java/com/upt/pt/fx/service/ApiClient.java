package com.upt.pt.fx.service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

	private static final String BASE_URL = "http://localhost:8080";

	// Adicionei um timeout para evitar que a app congele se o servidor estiver
	// desligado
	private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();


	// GET (Raw String) 

	public static String get(String path) throws Exception {
		System.out.println("API GET (String): " + path);
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).GET().build();

		HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
		tratarErro(resp);

		if (resp.body() == null)
			return "";
		return resp.body();
	}

	// GET ONE (JSONObject)

	public static JSONObject getJson(String path) throws Exception {
		String responseBody = get(path); // Reutiliza o método get() acima
		if (responseBody.isBlank())
			return new JSONObject();
		return new JSONObject(responseBody);
	}

	// GET LIST (JSONArray)

	public static JSONArray getArray(String path) throws Exception {
		String responseBody = get(path); // Reutiliza o método get() acima
		if (responseBody.isBlank())
			return new JSONArray();
		return new JSONArray(responseBody);
	}


	// POST (Criar / Login)

	public static JSONObject post(String path, JSONObject json) throws Exception {
		System.out.println("API POST: " + path);
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json.toString()))
				.build();

		HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
		tratarErro(resp);

		if (resp.body() == null || resp.body().isBlank())
			return new JSONObject();
		return new JSONObject(resp.body());
	}


	// PUT (Atualizar)

	public static JSONObject put(String path, JSONObject json) throws Exception {
		System.out.println("API PUT: " + path);
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path))
				.header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
				.build();

		HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
		tratarErro(resp);

		if (resp.body() == null || resp.body().isBlank())
			return new JSONObject();
		return new JSONObject(resp.body());
	}


	// DELETE (Apagar)

	public static JSONObject delete(String path) throws Exception {
		System.out.println("API DELETE: " + path);
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).DELETE().build();

		HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
		tratarErro(resp);

		if (resp.body() == null || resp.body().isBlank())
			return new JSONObject();
		// Tenta devolver JSON se houver, senão devolve vazio
		try {
			return new JSONObject(resp.body());
		} catch (Exception e) {
			return new JSONObject();
		}
	}


	// AUXILIAR: TRATAMENTO DE ERROS

	private static void tratarErro(HttpResponse<String> resp) throws Exception {
		if (resp.statusCode() >= 300) {
			System.err.println("❌ ERRO API (" + resp.statusCode() + ") em " + resp.uri());
			System.err.println("❌ BODY: " + resp.body());

			// Tenta extrair mensagem de erro do JSON se existir
			String msgErro = "Erro desconhecido";
			try {
				JSONObject erroJson = new JSONObject(resp.body());
				if (erroJson.has("message"))
					msgErro = erroJson.getString("message");
				else if (erroJson.has("error"))
					msgErro = erroJson.getString("error");
			} catch (Exception e) {
				msgErro = resp.body(); // Se não for JSON, usa o texto todo
			}

			throw new Exception("Erro API: " + msgErro);
		}
	}
}