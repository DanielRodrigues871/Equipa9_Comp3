package com.upt.pt.fx.controller;

import com.upt.pt.SceneManager;
import com.upt.pt.fx.service.ApiClient;
import com.upt.pt.fx.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class RepresentantePropostasController {

    // --- Componentes da Tabela (têm de ter o mesmo fx:id do FXML) ---
    @FXML private TableView<PropostaTabela> tabela;
    @FXML private TableColumn<PropostaTabela, String> colId;
    @FXML private TableColumn<PropostaTabela, String> colTitulo;
    @FXML private TableColumn<PropostaTabela, String> colEstado;
    @FXML private TableColumn<PropostaTabela, String> colVagas;
    @FXML private TableColumn<PropostaTabela, String> colDuracao;

    @FXML
    public void initialize() {
        configurarColunas();
        carregar();
    }

    private void configurarColunas() {
        // Liga as colunas aos "Getters" da classe PropostaTabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colVagas.setCellValueFactory(new PropertyValueFactory<>("vagas"));
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));
    }

    @FXML
    public void carregar() {
        try {
            String repId = UserSession.getId();
            
            // 1. Buscar dados à API
            JSONArray array = ApiClient.getArray("/api/propostas/representante/" + repId);

            ObservableList<PropostaTabela> lista = FXCollections.observableArrayList();

            // 2. Transformar JSON em objetos para a tabela
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                PropostaTabela p = new PropostaTabela(
                    String.valueOf(obj.get("id")),         // Garante String mesmo que venha Int
                    obj.getString("titulo"),
                    obj.optString("status", "Pendente"),   // Backend "status" -> Tabela "estado"
                    String.valueOf(obj.optInt("vagasDisponiveis", 0)),
                    String.valueOf(obj.optInt("duracaoMeses", 0))
                );
                lista.add(p);
            }

            // 3. Encher a tabela
            tabela.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar as propostas.");
        }
    }

    @FXML
    public void editar() {
        // Pegar o objeto selecionado na linha
        PropostaTabela selecionada = tabela.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta("Aviso", "Selecione uma proposta na tabela para editar.");
            return;
        }

        try {
            // Guardar ID na sessão e mudar de ecrã
            UserSession.setPropostaEditarId(selecionada.getId());
            System.out.println("A editar proposta ID: " + selecionada.getId());

            SceneManager.changeScene("representante_editar_proposta.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao abrir edição.");
        }
    }

    @FXML
    public void criarNova() {
        SceneManager.changeScene("representante_criar_proposta.fxml");
    }

    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_representante.fxml");
    }

    // Auxiliar para mostrar mensagens (já que removemos o statusLabel)
    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // =============================================================
    // CLASSE MODELO PARA A TABELA (NECESSÁRIA)
    // =============================================================
    public static class PropostaTabela {
        private final String id;
        private final String titulo;
        private final String estado;
        private final String vagas;
        private final String duracao;

        public PropostaTabela(String id, String titulo, String estado, String vagas, String duracao) {
            this.id = id;
            this.titulo = titulo;
            this.estado = estado;
            this.vagas = vagas;
            this.duracao = duracao;
        }

        public String getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getEstado() { return estado; }
        public String getVagas() { return vagas; }
        public String getDuracao() { return duracao; }
    }
}