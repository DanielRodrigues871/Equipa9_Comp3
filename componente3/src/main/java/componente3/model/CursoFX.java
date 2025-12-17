package componente3.model;

public class CursoFX {

    private String id;
    private String nome;
    private String sigla;
    private String area;
    private int duracaoAnos;

    public CursoFX(String id, String nome, String sigla, String area, int duracaoAnos) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.area = area;
        this.duracaoAnos = duracaoAnos;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getSigla() { return sigla; }
    public String getArea() { return area; }
    public int getDuracaoAnos() { return duracaoAnos; }
}
