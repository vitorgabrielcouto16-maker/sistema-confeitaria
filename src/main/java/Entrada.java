import java.time.LocalDate;

public class  Entrada extends MovimentoCaixa {
    public Entrada( LocalDate data, double valor, String descricao) {
        super(data, valor, descricao);
    }

    public Entrada(int id, LocalDate data, double valor, String descricao) {
        super(id, data, valor, descricao);
    }

    @Override
    public double getValorComSinal() {
        return getValor();
    }
}
