public class Cliente {
    private String nome;
    private String telefone;
    private int idCliente;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public Cliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public Cliente(String nome, String telefone, int idCliente) {
        this.nome = nome;
        this.telefone = telefone;
        this.idCliente = idCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }
}
