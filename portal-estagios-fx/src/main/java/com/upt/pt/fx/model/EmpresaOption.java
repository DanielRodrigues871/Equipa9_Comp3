package com.upt.pt.fx.model;

public class EmpresaOption {
    private String id;
    private String nome;

    public EmpresaOption(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // O JavaFX usa este método para decidir o que mostrar na lista
    @Override
    public String toString() {
        return nome;
    }
}