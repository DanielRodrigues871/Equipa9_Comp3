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


public class CoordenadorCriarOfertaController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtArea;          
    @FXML private TextField txtLocalizacao;    
    @FXML private TextArea txtDescricao;
    @FXML private TextArea txtRequisitos;      
    @FXML private ComboBox<JSONObject> cbEmpresa;
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
        spDuracao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 6));
        spVagas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));
    }

    private void configurarCombos() {
        // Tipos: CURRICULAR, EXTRACURRICULAR, VERÄO
        cbTipo.setItems(FXCollections.observableArrayList("CURRICULAR", "EXTRACURRICULAR", "VERÃO"));
        cbTipo.getSelectionModel().selectFirst();

        cbEmpresa.setConverter(new StringConverter<>() {
            @Override
            public String toString(JSONObject empresa) {
                return empresa != null ? empresa.optString("nome", "Sem Nome") : "";
            }
            @Override
            public JSONObject fromString(String string) { return null; }
        });
    }

    private void carregarEmpresas() {
        try {
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
        if (txtTitulo.getText().isEmpty() || cbEmpresa.getValue() == null || dpInicio.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha o Título, escolha a Empresa e a Data de Início.");
            return;
        }

        try {
            String empresaId = cbEmpresa.getValue().getString("id");
            String coordenadorId = UserSession.getId();
            
            String endpoint = String.format("/api/ofertas?empresaId=%s&coordenadorId=%s", 
                                            empresaId, coordenadorId);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("titulo", txtTitulo.getText());
            jsonBody.put("area", txtArea.getText());           // NOVO
            jsonBody.put("localizacao", txtLocalizacao.getText()); // NOVO
            jsonBody.put("descricao", txtDescricao.getText());
            jsonBody.put("requisitos", txtRequisitos.getText());   // NOVO
            jsonBody.put("tipo", cbTipo.getValue());
            jsonBody.put("duracaoMeses", spDuracao.getValue());
            jsonBody.put("numeroVagas", spVagas.getValue());
            jsonBody.put("dataInicio", dpInicio.getValue().toString());
            
            if (dpFim.getValue() != null) {
                jsonBody.put("dataFim", dpFim.getValue().toString());
            }

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
        txtArea.clear();           
        txtLocalizacao.clear();    
        txtDescricao.clear();
        txtRequisitos.clear();    
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