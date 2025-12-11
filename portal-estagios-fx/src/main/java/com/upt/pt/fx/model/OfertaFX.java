package com.upt.pt.fx.model;

/**
 * Modelo usado pela UI para representar uma oferta.
 * Possui dois construtores para os vários locais onde é instanciado:
 *  - id, titulo, duracaoMeses, numeroVagas
 *  - id, titulo, estado, duracaoMeses, vagasDisponiveis
 */
public class OfertaFX {

    private String id;
    private String titulo;
    // opcional
    private String estado;
 
    private int duracaoMeses;
    private int numeroVagas;
    private String empresa;

    // Construtor usado em consultarOfertas (id, titulo, duracao, numeroVagas)
    public OfertaFX(String id, String titulo, int duracaoMeses, int numeroVagas, String empresa) {
        this.id = id;
        this.titulo = titulo;
        this.duracaoMeses = duracaoMeses;
        this.numeroVagas = numeroVagas;
        this.empresa = empresa;
    }

    // Construtor usado em listarOfertasCoordenador (id, titulo, estado, duracao, vagas)
    public OfertaFX(String id, String titulo, String estado, int duracaoMeses, int numeroVagas) {
        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
        this.duracaoMeses = duracaoMeses;
        this.numeroVagas = numeroVagas;
    }

    // getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getEstado() { return estado; }
    public int getDuracaoMeses() { return duracaoMeses; }
    public int getNumeroVagas() { return numeroVagas; }
    public String getEmpresa() { return empresa; }

    // conveniência (compatibilidade com "vagas" ou "vagasDisponiveis" usados por PropertyValueFactory)
    public int getVagas() { return numeroVagas; }
    public int getVagasDisponiveis() { return numeroVagas; }
}
