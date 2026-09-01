import java.time.LocalDate;

public class Saida extends MovimentoCaixa {
    public Saida(LocalDate data, double valor, String descricao) {
        super(data, valor, descricao);
    }

    public Saida(int id, LocalDate data, double valor, String descricao) {
        super(id, data, valor, descricao);
    }

    @Override
    public double getValorComSinal() {
        return -getValor();
    }
}
