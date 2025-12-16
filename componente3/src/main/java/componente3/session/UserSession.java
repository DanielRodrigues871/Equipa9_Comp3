package componente3.session;

public class UserSession {

    private static String id;
    private static String nome;
    private static String email;
    private static String tipo;

    private static String ofertaSelecionada; // <<–– novo campo

    public static void setUser(String idU, String n, String e, String t) {
        id = idU;
        nome = n;
        email = e;
        tipo = t;
    }

    public static String getId() { return id; }
    public static String getNome() { return nome; }
    public static String getEmail() { return email; }
    public static String getTipo() { return tipo; }

    // NOVOS GET/SET
    public static void setOfertaSelecionada(String idOferta) {
        ofertaSelecionada = idOferta;
    }

    public static String getOfertaSelecionada() {
        return ofertaSelecionada;
    }

    public static void logout() {
        id = nome = email = tipo = ofertaSelecionada = null;
        empresaId = null;

    }
    
    private static String empresaId;
    public static void setEmpresaId(String id) { empresaId = id; }
    public static String getEmpresaId() { return empresaId; }

    
}
