package componente3.controller;

import componente3.SceneManager;
import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONObject;

public class RepresentanteCriarPropostaController {

    @FXML private TextField tituloField;
    @FXML private TextArea descricaoArea;
    @FXML private TextArea requisitosArea;
    @FXML private TextField localizacaoField;
    @FXML private TextField duracaoField;
    @FXML private TextField vagasField;
    @FXML private ComboBox<String> tipoCombo;

    @FXML private Label errorLabel;

    // ==========================
    //  INITIALIZE
    // ==========================
    @FXML
    public void initialize() {
        configurarCombos();
        limparErros();
    }

    private void configurarCombos() {
        tipoCombo.getItems().setAll("CURRICULAR", "EXTRA_CURRICULAR");
        tipoCombo.getSelectionModel().selectFirst();
    }

    private void limparErros() {
        errorLabel.setText("");
    }

    // ==========================
    //  AÇÕES DO UTILIZADOR
    // ==========================
    @FXML
    public void criar() {
        limparErros();

        // 1. Validação básica
        if (tituloField.getText().isBlank()
                || tipoCombo.getValue() == null
                || duracaoField.getText().isBlank()
                || vagasField.getText().isBlank()) {

            errorLabel.setText("Preencha Título, Tipo, Duração e Vagas.");
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
            // 2. Preparar JSON com os dados da proposta
            JSONObject json = new JSONObject();
            json.put("titulo", tituloField.getText());
            json.put("descricao", descricaoArea.getText());
            json.put("requisitos", requisitosArea.getText());
            json.put("localizacao", localizacaoField.getText());
            json.put("duracaoMeses", duracao);
            json.put("vagasDisponiveis", vagas);
            json.put("tipo", tipoCombo.getValue());

            // 3. IDs de empresa e representante (query params)
            String empresaId = UserSession.getEmpresaId();
            String representanteId = UserSession.getId();

            String endpoint = String.format(
                    "/api/propostas?empresaId=%s&representanteId=%s",
                    empresaId, representanteId
            );

            // 4. Enviar ao backend
            JSONObject resposta = ApiClient.post(endpoint, json);

            // 5. Feedback de sucesso (podes validar se veio "id" na resposta)
            if (resposta.has("id")) {
                errorLabel.setText("Proposta criada com sucesso.");
                limparFormulario();
                // se quiseres voltar logo ao dashboard:
                // SceneManager.changeScene("dashboard_representante.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erro ao criar proposta.");
        }
    }

    @FXML
    public void limparFormulario() {
        tituloField.clear();
        descricaoArea.clear();
        requisitosArea.clear();
        localizacaoField.clear();
        duracaoField.clear();
        vagasField.clear();
        tipoCombo.getSelectionModel().selectFirst();
        limparErros();
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }
}
