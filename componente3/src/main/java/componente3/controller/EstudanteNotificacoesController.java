package componente3.controller;

import componente3.service.ApiClient;
import componente3.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.JSONArray;
import org.json.JSONObject;

public class EstudanteNotificacoesController {

    @FXML
    private ListView<JSONObject> listaNotificacoes;

    @FXML
    public void initialize() {
        configurarLista();
        carregarNotificacoes();
    }

    private void configurarLista() {
        // Criar uma célula personalizada para mostrar Título + Mensagem
        listaNotificacoes.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(JSONObject item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Título em Negrito
                    String tituloTxt = item.optString("titulo", "Sem título");
                    Text titulo = new Text(tituloTxt + "\n");
                    titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    // Mensagem normal
                    String msgTxt = item.optString("mensagem", "");
                    Text mensagem = new Text(msgTxt);
                    
                    // Estado (Lida/Não lida)
                    if (!item.optBoolean("lida")) {
                        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: #2980b9;"); // Azul se não lida
                    }

                    TextFlow flow = new TextFlow(titulo, mensagem);
                    setGraphic(flow);
                }
            }
        });
    }

    private void carregarNotificacoes() {
        try {
            String idEstudante = UserSession.getId();
            // Chama o endpoint que criámos
            JSONArray jsonArray = ApiClient.getArray("/api/notificacoes/estudante/" + idEstudante);

            ObservableList<JSONObject> items = FXCollections.observableArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                items.add(jsonArray.getJSONObject(i));
            }
            
            listaNotificacoes.setItems(items);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar notificações.");
            // Se der erro, mostramos vazio ou uma mensagem de erro na lista
        }
    }
    
    @FXML
    public void marcarComoLidas() {
        // Exemplo: Percorrer a lista e chamar a API para marcar como lida
        // Implementação futura
        System.out.println("Funcionalidade a implementar: Marcar todas como lidas");
    }
}
