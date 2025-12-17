package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EstudanteNotificacoesController {

	@FXML
	private TableView<JSONObject> tabelaNotificacoes;
	@FXML
	private TableColumn<JSONObject, Boolean> colSelecionar;
	@FXML
	private TableColumn<JSONObject, String> colTitulo;
	@FXML
	private TableColumn<JSONObject, String> colMensagem;
	@FXML
	private TableColumn<JSONObject, String> colData;
	@FXML
	private TableColumn<JSONObject, String> colEstado;
	@FXML
	private TableColumn<JSONObject, Void> colAcao;

	// Mapa para controlar quais linhas estão selecionadas (checkbox)
	private Map<String, SimpleBooleanProperty> selectionMap = new HashMap<>();

	@FXML
	public void initialize() {
		configurarColunas();
		carregarNotificacoes();
	}

	@FXML
	public void atualizar() {
		carregarNotificacoes();
	}

	private void configurarColunas() {
		// 1. Coluna de Seleção (Checkbox)
		colSelecionar.setCellValueFactory(data -> {
			String id = data.getValue().optString("id");
			selectionMap.putIfAbsent(id, new SimpleBooleanProperty(false));
			return selectionMap.get(id);
		});
		colSelecionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecionar));
		tabelaNotificacoes.setEditable(true); // Obrigatório para editar checkboxes

		// 2. Título (Proteção contra nulos)
		colTitulo.setCellValueFactory(data -> {
			String t = data.getValue().optString("titulo");
			if (t == null || t.isEmpty() || t.equals("null"))
				t = "Nova Notificação";
			return new SimpleStringProperty(t);
		});

		// 3. Mensagem
		colMensagem.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("mensagem", "")));

		// 4. Data (Tenta ler várias chaves possíveis)
		colData.setCellValueFactory(data -> {
			JSONObject json = data.getValue();
			String rawDate = "";

			// Verifica qual é o nome que o Backend está a enviar
			if (json.has("dataCriacao") && !json.isNull("dataCriacao")) {
				rawDate = json.getString("dataCriacao");
			} else if (json.has("data_criacao") && !json.isNull("data_criacao")) {
				rawDate = json.getString("data_criacao");
			}

			return new SimpleStringProperty(formatarData(rawDate));
		});

		// 5. Estado (Formatação Visual)
		colEstado.setCellValueFactory(data -> {
			// O backend pode enviar booleano (true/false) ou inteiro (1/0)
			boolean lida = data.getValue().optBoolean("lida", false);
			int lidaInt = data.getValue().optInt("lida", 0);

			// Se for true ou 1, conta como lida
			boolean isLida = lida || (lidaInt == 1);

			return new SimpleStringProperty(isLida ? "Lida" : "Nova");
		});

		// Estilização do texto do estado
		colEstado.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					if ("Nova".equals(item)) {
						setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;"); // Azul e Negrito
					} else {
						setStyle("-fx-text-fill: #7f8c8d;"); // Cinzento
					}
				}
			}
		});

		// 6. Botão de Ação Individual
		colAcao.setCellFactory(param -> new TableCell<>() {
			private final Button btnLer = new Button("Marcar Lida");
			{
				btnLer.setStyle(
						"-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand;");
				btnLer.setOnAction(event -> {
					JSONObject notif = getTableView().getItems().get(getIndex());
					marcarIndividual(notif);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					JSONObject notif = getTableView().getItems().get(getIndex());
					// Verifica estado (boolean ou int)
					boolean lida = notif.optBoolean("lida", false) || notif.optInt("lida", 0) == 1;

					if (!lida) {
						setGraphic(btnLer);
					} else {
						setGraphic(null);
					}
				}
			}
		});
	}

	private void carregarNotificacoes() {
		try {
			selectionMap.clear();
			String idEstudante = UserSession.getId();

			// Busca dados da API
			JSONArray jsonArray = ApiClient.getArray("/api/notificacoes/estudante/" + idEstudante);

			// DEBUG: Verifique na consola se aparece "dataCriacao" ou "data_criacao"
			System.out.println("JSON Notificações: " + jsonArray.toString());

			ObservableList<JSONObject> items = FXCollections.observableArrayList();
			for (int i = 0; i < jsonArray.length(); i++) {
				items.add(jsonArray.getJSONObject(i));
			}

			tabelaNotificacoes.setItems(items);
			tabelaNotificacoes.refresh();

		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Aviso", "Não foi possível carregar as notificações. Verifique se o backend está a correr.");
		}
	}

	// Método robusto para formatar datas (ISO ou SQL)
	private String formatarData(String dataIso) {
		if (dataIso == null || dataIso.isEmpty() || dataIso.equals("-"))
			return "-";

		try {
			// Se vier formato SQL com espaço "2025-12-03 23:52...", substitui por "T"
			dataIso = dataIso.replace(" ", "T");

			// Corta milissegundos excessivos se existirem (JavaFX parser falha com muitos números)
			// Mantém apenas yyyy-MM-ddTHH:mm:ss (19 caracteres)
			if (dataIso.length() > 19) {
				dataIso = dataIso.substring(0, 19);
			}

			LocalDateTime data = LocalDateTime.parse(dataIso);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			return data.format(formatter);

		} catch (Exception e) {
			System.err.println("Erro ao formatar data: " + dataIso);
			return dataIso; // Se falhar, mostra como veio
		}
	}

	private void marcarIndividual(JSONObject notif) {
		try {
			String id = notif.getString("id");
			ApiClient.post("/api/notificacoes/" + id + "/ler", new JSONObject());

			// Atualiza localmente
			notif.put("lida", true);
			tabelaNotificacoes.refresh();

		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Erro", "Erro ao atualizar notificação.");
		}
	}

	@FXML
	public void marcarSelecionadasComoLidas() {
		boolean alguma = false;
		for (JSONObject notif : tabelaNotificacoes.getItems()) {
			String id = notif.optString("id");
			SimpleBooleanProperty checked = selectionMap.get(id);

			if (checked != null && checked.get()) {
				boolean jaLida = notif.optBoolean("lida", false) || notif.optInt("lida", 0) == 1;

				if (!jaLida) {
					alguma = true;
					marcarIndividual(notif);
				}
				checked.set(false); // Remove o check
			}
		}

		if (!alguma) {
			showAlert("Info", "Nenhuma notificação nova selecionada.");
		} else {
			tabelaNotificacoes.refresh();
		}
	}

	private void showAlert(String titulo, String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.show();
	}
}