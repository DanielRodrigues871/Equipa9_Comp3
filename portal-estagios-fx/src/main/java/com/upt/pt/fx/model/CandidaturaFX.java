package com.upt.pt.fx.model;

public class CandidaturaFX {

    private String id;
    private String estudanteNome;
    private String estado;
    private String data;

    public CandidaturaFX(String id, String estudanteNome, String estado, String data) {
        this.id = id;
        this.estudanteNome = estudanteNome;
        this.estado = estado;
        this.data = data;
    }

    public String getId() { return id; }
    public String getEstudanteNome() { return estudanteNome; }
    public String getEstado() { return estado; }
    public String getData() { return data; }
}
