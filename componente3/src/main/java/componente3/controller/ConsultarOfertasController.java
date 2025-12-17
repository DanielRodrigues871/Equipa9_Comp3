package componente3.controller;

import componente3.SceneManager;
import componente3.model.OfertaFX;
import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultarOfertasController {

    @FXML
    private TableView<OfertaFX> tabelaOfertas;

    @FXML
    public void initialize() {
        carregarOfertas();
    }

    private void carregarOfertas() {
        try {
            JSONArray arr = ApiClient.getArray("/api/ofertas/status/APROVADO");

            List<OfertaFX> lista = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                lista.add(new OfertaFX(
                        o.getString("id"),
                        o.getString("titulo"),
                        o.getInt("duracaoMeses"),
                        o.getInt("numeroVagas")
                ));
            }

            tabelaOfertas.setItems(FXCollections.observableArrayList(lista));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    public void abrirCandidatura() {
        OfertaFX sel = tabelaOfertas.getSelectionModel().getSelectedItem();

        if (sel == null) return;

        // Guardar ID da oferta se for preciso
        UserSession.setOfertaSelecionada(sel.getId());


        SceneManager.changeScene("candidatar.fxml");
    }

    @FXML
    public void voltarDashboard() {
        SceneManager.changeScene("dashboard_estudante.fxml");
    }
}
