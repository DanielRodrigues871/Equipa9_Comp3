package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class RepresentanteCriarPropostaController {

	@FXML
	private TextField tituloField;
	@FXML
	private TextField areaField; 
	@FXML
	private TextArea descricaoArea;
	@FXML
	private TextArea requisitosArea;
	@FXML
	private TextField localizacaoField;
	@FXML
	private TextField duracaoField;
	@FXML
	private TextField vagasField;
	@FXML
	private ComboBox<String> tipoCombo;
	@FXML
	private ComboBox<String> remuneradoCombo; 

	@FXML
	private Label errorLabel;

	@FXML
	public void initialize() {
		configurarCombos();
		limparErros();
	}

	private void configurarCombos() {
		// Tipos completos: CURRICULAR, EXTRACURRICULAR, VERÃO
		tipoCombo.getItems().setAll("CURRICULAR", "EXTRACURRICULAR", "VERÃO");
		tipoCombo.getSelectionModel().selectFirst();

		// Remunerado: SIM, NÃO
		remuneradoCombo.getItems().setAll("SIM", "NÃO");
		remuneradoCombo.getSelectionModel().selectFirst();
	}

	private void limparErros() {
		errorLabel.setText("");
	}

	@FXML
	public void criar() {
		limparErros();

		if (tituloField.getText().isBlank() || tipoCombo.getValue() == null || remuneradoCombo.getValue() == null
				|| duracaoField.getText().isBlank() || vagasField.getText().isBlank()) {

			errorLabel.setText("Preencha Título, Tipo, Remunerado, Duração e Vagas.");
			return;
		}

		int duracao;
		int vagas;

		try {
			duracao = Integer.parseInt(duracaoField.getText());
			vagas = Integer.parseInt(vagasField.getText());
		} catch (NumberFormatException nfe) {
			errorLabel.setText("Duração e Vagas têm de ser números inteiros.");
			return;
		}

		try {
			JSONObject json = new JSONObject();
			json.put("titulo", tituloField.getText());
			json.put("area", areaField.getText()); 
			json.put("descricao", descricaoArea.getText());
			json.put("requisitos", requisitosArea.getText());
			json.put("localizacao", localizacaoField.getText());
			json.put("duracaoMeses", duracao);
			json.put("vagasDisponiveis", vagas);
			json.put("tipo", tipoCombo.getValue());
			json.put("remunerado", remuneradoCombo.getValue()); 

			String empresaId = UserSession.getEmpresaId();
			String representanteId = UserSession.getId();

			String endpoint = String.format("/api/propostas?empresaId=%s&representanteId=%s", empresaId,
					representanteId);

			JSONObject resposta = ApiClient.post(endpoint, json);

			if (resposta.has("id")) {
				errorLabel.setText("Proposta criada com sucesso.");
				limparFormulario();
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorLabel.setText("Erro ao criar proposta.");
		}
	}

	@FXML
	public void limparFormulario() {
		tituloField.clear();
		areaField.clear(); // NOVO
		descricaoArea.clear();
		requisitosArea.clear();
		localizacaoField.clear();
		duracaoField.clear();
		vagasField.clear();
		tipoCombo.getSelectionModel().selectFirst();
		remuneradoCombo.getSelectionModel().selectFirst(); // NOVO
		limparErros();
	}

	@FXML
	public void voltar() {
		SceneManager.changeScene("dashboard_representante.fxml");
	}
}