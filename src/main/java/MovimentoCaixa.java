import java.time.LocalDate;

public abstract class  MovimentoCaixa {
    private int id;
    private LocalDate data;
    private double valor;
    private String descricao;

    public MovimentoCaixa(int id, LocalDate data, double valor, String descricao) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }

    public MovimentoCaixa(LocalDate data, double valor, String descricao) {
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;

    }

    public LocalDate getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public abstract double getValorComSinal();
}
