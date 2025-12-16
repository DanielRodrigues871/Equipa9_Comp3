package com.upt.pt.fx.controller;

import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class CoordenadorCriarOfertaController {

    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<JSONObject> cbEmpresa; // Guarda o JSON da empresa, mostra o Nome
    @FXML private ComboBox<String> cbTipo;
    @FXML private Spinner<Integer> spDuracao;
    @FXML private Spinner<Integer> spVagas;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;

    @FXML
    public void initialize() {
        configurarSpinners();
        configurarCombos();
        carregarEmpresas();
    }

    private void configurarSpinners() {
        // Define valores mínimos, máximos e iniciais
        spDuracao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 6)); // 1 a 24 meses, default 6
        spVagas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));   // 1 a 50 vagas, default 1
    }

    private void configurarCombos() {
        // 1. Tipos de Estágio (Hardcoded ou vindo da API)
        cbTipo.setItems(FXCollections.observableArrayList("CURRICULAR", "PROFISSIONAL", "VERAO"));
        cbTipo.getSelectionModel().selectFirst();

        // 2. Conversor para a ComboBox de Empresas
        // Isto faz com que a combobox mostre o "nome" do JSON, mas guarde o objeto todo
        cbEmpresa.setConverter(new StringConverter<>() {
            @Override
            public String toString(JSONObject empresa) {
                return empresa != null ? empresa.optString("nome", "Sem Nome") : "";
            }

            @Override
            public JSONObject fromString(String string) {
                return null; // Não precisamos disto para este caso (readonly selection)
            }
        });
    }

    private void carregarEmpresas() {
        try {
            // Requer endpoint GET /api/empresas
            JSONArray jsonArray = ApiClient.getArray("/api/empresas");
            
            ObservableList<JSONObject> listaEmpresas = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                listaEmpresas.add(jsonArray.getJSONObject(i));
            }
            cbEmpresa.setItems(listaEmpresas);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar a lista de empresas.");
        }
    }

    @FXML
    public void salvarOferta() {
        // 1. Validação Básica
        if (txtTitulo.getText().isEmpty() || cbEmpresa.getValue() == null || dpInicio.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha o Título, escolha a Empresa e a Data de Início.");
            return;
        }

        try {
            // 2. Preparar os IDs para o URL (Query Params)
            String empresaId = cbEmpresa.getValue().getString("id");
            String coordenadorId = UserSession.getId();
            
            // Construir URL: /api/ofertas?empresaId=X&coordenadorId=Y
            // Nota: Se quiser adicionar Area ou Curso, adicione &areaId=... aqui
            String endpoint = String.format("/api/ofertas?empresaId=%s&coordenadorId=%s", 
                                            empresaId, coordenadorId);

            // 3. Preparar o Body JSON (Dados da oferta)
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("titulo", txtTitulo.getText());
            jsonBody.put("descricao", txtDescricao.getText());
            jsonBody.put("tipo", cbTipo.getValue());
            jsonBody.put("duracaoMeses", spDuracao.getValue());
            jsonBody.put("numeroVagas", spVagas.getValue());
            jsonBody.put("dataInicio", dpInicio.getValue().toString()); // Formato YYYY-MM-DD
            
            if (dpFim.getValue() != null) {
                jsonBody.put("dataFim", dpFim.getValue().toString());
            }

            // 4. Enviar ao Backend
            JSONObject resposta = ApiClient.post(endpoint, jsonBody);

            if (resposta.has("id")) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Oferta criada com sucesso!");
                limparFormulario();
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao criar oferta.\n" + e.getMessage());
        }
    }

    @FXML
    public void limparFormulario() {
        txtTitulo.clear();
        txtDescricao.clear();
        cbEmpresa.getSelectionModel().clearSelection();
        cbTipo.getSelectionModel().selectFirst();
        spDuracao.getValueFactory().setValue(6);
        spVagas.getValueFactory().setValue(1);
        dpInicio.setValue(null);
        dpFim.setValue(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}