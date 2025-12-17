package componente3.controller;

import componente3.service.ApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoordenadorAcademicoController {

    // TAB 1: Estudantes
    @FXML private ComboBox<JSONObject> cbCursosLista;
    @FXML private TableView<JSONObject> tabelaEstudantes;
    @FXML private TableColumn<JSONObject, String> colNomeEst;
    @FXML private TableColumn<JSONObject, String> colEmailEst;
    @FXML private TableColumn<JSONObject, String> colNumEst;
    @FXML private TableColumn<JSONObject, String> colMedia;

    // TAB 2: Criar Curso
    @FXML private TextField txtNomeCurso;
    @FXML private TextField txtCodigoCurso;
    @FXML private TextField txtGrau;
    @FXML private TextField txtDepId;
    @FXML private Spinner<Integer> spDuracao;

    @FXML
    public void initialize() {
        // Configurar Tabela Estudantes
        colNomeEst.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("nome")));
        colEmailEst.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("email")));
        colNumEst.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().optString("numeroEstudante")));
        colMedia.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().optDouble("media"))));

        // Configurar Spinner
        spDuracao.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6, 3));

        // Configurar ComboBox de Cursos (Para mostrar o nome mas guardar o objeto)
        cbCursosLista.setConverter(new StringConverter<>() {
            @Override public String toString(JSONObject c) { return c != null ? c.optString("nome") : ""; }
            @Override public JSONObject fromString(String s) { return null; }
        });

        carregarCursosNaCombo();
    }

    private void carregarCursosNaCombo() {
        try {
            JSONArray arr = ApiClient.getArray("/api/cursos"); // Endpoint necessário: GET /api/cursos
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for(int i=0; i<arr.length(); i++) lista.add(arr.getJSONObject(i));
            cbCursosLista.setItems(lista);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void carregarEstudantes() {
        JSONObject cursoSelecionado = cbCursosLista.getValue();
        if (cursoSelecionado == null) return;

        try {
            String cursoId = cursoSelecionado.getString("id");
            // API: /api/estudantes/curso/{id}
            JSONArray arr = ApiClient.getArray("/api/estudantes/curso/" + cursoId);
            
            ObservableList<JSONObject> lista = FXCollections.observableArrayList();
            for(int i=0; i<arr.length(); i++) lista.add(arr.getJSONObject(i));
            tabelaEstudantes.setItems(lista);
            
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void registarCurso() {
        try {
            String nome = txtNomeCurso.getText();
            String cod = txtCodigoCurso.getText();
            String grau = txtGrau.getText();
            String depId = txtDepId.getText();
            int duracao = spDuracao.getValue();

            if (nome.isBlank() || depId.isBlank()) {
                showAlert("Nome e Departamento são obrigatórios.");
                return;
            }

            JSONObject json = new JSONObject();
            json.put("nome", nome);
            json.put("codigo", cod);
            json.put("grau", grau);
            json.put("duracaoAnos", duracao);

            // API: /api/cursos?departamentoId=...
            ApiClient.post("/api/cursos?departamentoId=" + depId, json);
            
            showAlert("Curso criado com sucesso!");
            
            // Limpar e recarregar combo
            txtNomeCurso.clear();
            carregarCursosNaCombo();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro ao criar curso: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION); a.setContentText(msg); a.show();
    }
}
