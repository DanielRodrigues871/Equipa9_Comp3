package com.upt.pt.fx.model;

public class DepartamentoOption {

    private String id;
    private String nome;

    public DepartamentoOption(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() { return id; }

    @Override
    public String toString() {
        return nome;
    }
}
