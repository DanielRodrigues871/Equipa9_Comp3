package componente3.controller;

import componente3.SceneManager;
import componente3.model.CursoFX;
import componente3.service.ApiClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import componente3.session.UserSession;

import java.util.ArrayList;
import java.util.List;

public class ListarCursosController {

    @FXML private TableView<CursoFX> tabelaCursos;
    @FXML private TableColumn<CursoFX, String> colNome;
    @FXML private TableColumn<CursoFX, String> colSigla;
    @FXML private TableColumn<CursoFX, String> colArea;
    @FXML private TableColumn<CursoFX, Integer> colDuracao;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracaoAnos"));

        carregarCursos();
    }

    private void carregarCursos() {
        try {
            JSONArray arr = ApiClient.getArray("/api/cursos");

            List<CursoFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);

                lista.add(new CursoFX(
                        c.getString("id"),
                        c.getString("nome"),
                        c.getString("sigla"),
                        c.getString("area"),
                        c.getInt("duracaoAnos")
                ));
            }

            tabelaCursos.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void verEstudantes() {
        CursoFX sel = tabelaCursos.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        // guardar id na sessão
        UserSession.setCursoSelecionado(sel.getId());

        SceneManager.changeScene("listar_estudantes.fxml");
    }


    @FXML
    public void voltar() {
        SceneManager.changeScene("dashboard_coordenador.fxml");
    }
}
