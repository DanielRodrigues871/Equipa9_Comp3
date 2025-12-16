package com.upt.pt.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmpresaFX {
    private final String id;
    private final StringProperty nome;
    private final StringProperty nif;
    private final StringProperty status;

    public EmpresaFX(String id, String nome, String nif, String status) {
        this.id = id;
        this.nome = new SimpleStringProperty(nome);
        this.nif = new SimpleStringProperty(nif);
        this.status = new SimpleStringProperty(status);
    }

    public String getId() { return id; }

    public StringProperty nomeProperty() { return nome; }
    public StringProperty nifProperty() { return nif; }
    public StringProperty statusProperty() { return status; }
}
