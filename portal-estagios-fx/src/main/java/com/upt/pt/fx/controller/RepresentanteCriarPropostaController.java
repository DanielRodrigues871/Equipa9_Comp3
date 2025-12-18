package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;


public class RepresentanteCriarPropostaController {

    @FXML private TextField tituloField;
    @FXML private TextField areaField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;
    @FXML private ComboBox<String> tipoCombo;
    @FXML private ComboBox<String> remuneradoCombo;

    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        configurarCombos();
        limparErros();
    }

    private void configurarCombos() {
        // Backend normalmente espera nomes sem acentos para ENUMs, vamos garantir compatibilidade
        tipoCombo.getItems().setAll("CURRICULAR", "EXTRACURRICULAR", "VERAO"); 
        tipoCombo.getSelectionModel().selectFirst();

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
            
            // Se o backend espera 'areasIds' (Lista), 'area' (String) pode ser ignorado ou dar erro.
            // Por agora enviamos, mas o ideal seria ter IDs reais.
            json.put("area", areaField.getText()); 
            
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", duracao);
            json.put("vagasDisponiveis", vagas);
            
            // Tratamento do Enum Tipo
            String tipoSelecionado = tipoCombo.getValue();
            // Garante que enviamos sem acentos se o utilizador escolher algo com acento visualmente
            if (tipoSelecionado.equals("VERÃO")) tipoSelecionado = "VERAO";
            json.put("tipo", tipoSelecionado);

            // Conversão de SIM/NÃO para true/false 
            boolean isRemunerado = "SIM".equals(remuneradoCombo.getValue());
            json.put("remunerado", isRemunerado); // Envia boolean, não String!

            // Configuração dos parâmetros da URL
            String empresaId = UserSession.getEmpresaId();
            String representanteId = UserSession.getId();

            if (empresaId == null) {
                errorLabel.setText("Erro: ID da Empresa não encontrado na sessão.");
                return;
            }

            // O parâmetro 'areaId' é opcional no controller backend, então podemos omitir se não tivermos ID
            String endpoint = String.format("/api/propostas?empresaId=%s&representanteId=%s", 
                    empresaId, representanteId);

            System.out.println("A enviar JSON: " + json.toString()); // Debug

            JSONObject resposta = ApiClient.post(endpoint, json);

            if (resposta.has("id")) {
                // Sucesso! Mostra alerta e limpa
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Proposta criada com sucesso!");
                alert.showAndWait();
                
                limparFormulario();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao criar proposta: " + e.getMessage());
        }
    }

    @FXML
    public void limparFormulario() {
        tituloField.clear();
        areaField.clear();
        descricaoArea.clear();
        requisitosArea.clear();
        localizacaoField.clear();
        duracaoField.clear();
        vagasField.clear();
        tipoCombo.getSelectionModel().selectFirst();
        remuneradoCombo.getSelectionModel().selectFirst();
        limparErros();
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}