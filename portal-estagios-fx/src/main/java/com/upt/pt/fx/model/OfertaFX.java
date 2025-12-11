package com.upt.pt.fx.model;

public class OfertaFX {
    private String id;
    private String titulo;
    private int duracaoMeses;
    private int numeroVagas;

    public OfertaFX(String id, String titulo, int duracaoMeses, int numeroVagas) {
        this.id = id;
        this.titulo = titulo;
        this.duracaoMeses = duracaoMeses;
        this.numeroVagas = numeroVagas;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getDuracaoMeses() { return duracaoMeses; }
    public int getNumeroVagas() { return numeroVagas; }
}
