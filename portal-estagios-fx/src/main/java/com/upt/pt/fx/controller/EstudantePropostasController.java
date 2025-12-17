package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.json.JSONArray;
import org.json.JSONObject;

public class EstudantePropostasController {

	@FXML
	private TableView<JSONObject> tabelaPropostas;
	@FXML
	private TableColumn<JSONObject, String> colTitulo;
	@FXML
	private TableColumn<JSONObject, String> colEmpresa;
	@FXML
	private TableColumn<JSONObject, String> colTipo;
	@FXML
	private TableColumn<JSONObject, Integer> colDuracao;
	@FXML
	private TableColumn<JSONObject, Integer> colVagas;

	@FXML
	public void initialize() {
		configurarColunas();
		carregarPropostas();
	}

	private void configurarColunas() {
		colTitulo.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().optString("titulo", "Sem Título")));
		colEmpresa.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().optString("empresaNome", "Anónimo")));
		colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("tipo", "-")));
		colDuracao.setCellValueFactory(
				data -> new SimpleIntegerProperty(data.getValue().optInt("duracaoMeses", 0)).asObject());
		colVagas.setCellValueFactory(
				data -> new SimpleIntegerProperty(data.getValue().optInt("vagasDisponiveis", 0)).asObject());
	}

	@FXML
	public void carregarPropostas() {
		try {
			// Endpoint para propostas aprovadas
			JSONArray jsonArray = ApiClient.getArray("/api/propostas/status/APROVADO");
			System.out.println("Propostas recebidas: " + jsonArray.length());

			ObservableList<JSONObject> lista = FXCollections.observableArrayList();
			for (int i = 0; i < jsonArray.length(); i++) {
				lista.add(jsonArray.getJSONObject(i));
			}
			tabelaPropostas.setItems(lista);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar as propostas.");
		}
	}

	@FXML
	public void candidatar() {
		JSONObject propostaSelecionada = tabelaPropostas.getSelectionModel().getSelectedItem();

		if (propostaSelecionada == null) {
			mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma proposta na lista primeiro.");
			return;
		}

		try {
			String propostaId = propostaSelecionada.getString("id");
			String estudanteId = UserSession.getId();

			String endpoint = String.format("/api/candidaturas/propostas?estudanteId=%s&propostaId=%s", estudanteId,
					propostaId);

			JSONObject jsonBody = new JSONObject();
			JSONObject response = ApiClient.post(endpoint, jsonBody);

			if (response.has("id")) {
				mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Candidatura submetida com sucesso!");
			} else {
				mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível candidatar.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao comunicar com o servidor.");
		}
	}

	@FXML
	public void verDetalhes() {
		JSONObject selecionada = tabelaPropostas.getSelectionModel().getSelectedItem();
		if (selecionada == null) {
			mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma proposta primeiro.");
			return;
		}

		StringBuilder detalhes = new StringBuilder();
		detalhes.append("Título: ").append(selecionada.optString("titulo")).append("\n");
		detalhes.append("Empresa: ").append(selecionada.optString("empresaNome", "N/A")).append("\n");
		detalhes.append("Tipo: ").append(selecionada.optString("tipo")).append("\n");
		detalhes.append("Duração: ").append(selecionada.optInt("duracaoMeses")).append(" meses\n");
		detalhes.append("Vagas: ").append(selecionada.optInt("vagasDisponiveis")).append("\n");
		detalhes.append("Remunerado: ").append(selecionada.optString("remunerado", "-")).append("\n");
		detalhes.append("Descrição: ").append(selecionada.optString("descricao", "Sem descrição."));

		showDetalhesAlert("Detalhes da Proposta", detalhes.toString());
	}

	@FXML
	public void voltarDashboard() {
		SceneManager.changeScene("dashboard_estudante.fxml");
	}

	private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensagem);
		alert.showAndWait();
	}

	private void showDetalhesAlert(String titulo, String conteudo) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);

		TextArea textArea = new TextArea(conteudo);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 0);

		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);
		alert.showAndWait();
	}
}