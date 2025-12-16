package com.upt.pt.fx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MenuEmpresasRepresentanteController {

    @FXML private AnchorPane contentPane;

    @FXML public void verEmpresa() { 
        loadView("ver_empresa.fxml"); 
    }

    @FXML public void editarEmpresa() { 
        loadView("editar_empresa.fxml"); 
    }

    @FXML public void voltar() { 
        loadView("dashboard_representante.fxml"); 
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            System.err.println("ERRO ao carregar: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
