import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RelatorioCaixa {

    private final MovimentoCaixaRepository movimentoCaixaRepository;

    public RelatorioCaixa(MovimentoCaixaRepository movimentoCaixaRepository) {
        this.movimentoCaixaRepository = movimentoCaixaRepository;
    }

    public double calcularSaldo() throws SQLException {
        List<MovimentoCaixa> movimentos = movimentoCaixaRepository.listarTodos();

        double saldo = 0;
        for (MovimentoCaixa movimento : movimentos) {
            saldo += movimento.getValorComSinal();
        }
        return saldo;
    }

    public double calcularSaldoDoMes(int ano, int mes) throws SQLException {
        List<MovimentoCaixa> movimentos = movimentoCaixaRepository.listarTodos();

        double saldo = 0;
        for (MovimentoCaixa movimento : movimentos) {
            LocalDate data = movimento.getData();
            if (data.getYear() == ano && data.getMonthValue() == mes) {
                saldo += movimento.getValorComSinal();
            }
        }
        return saldo;
    }

    public double calcularTotalEntradas(int ano, int mes) throws SQLException {
        List<MovimentoCaixa> movimentos = movimentoCaixaRepository.listarTodos();

        double total = 0;
        for (MovimentoCaixa movimento : movimentos) {
            LocalDate data = movimento.getData();
            if (movimento instanceof Entrada
                    && data.getYear() == ano
                    && data.getMonthValue() == mes) {
                total += movimento.getValorComSinal();
            }
        }
        return total;
    }

    public double calcularTotalSaidas(int ano, int mes) throws SQLException {
        List<MovimentoCaixa> movimentos = movimentoCaixaRepository.listarTodos();

        double total = 0;
        for (MovimentoCaixa movimento : movimentos) {
            LocalDate data = movimento.getData();
            if (movimento instanceof Saida
                    && data.getYear() == ano
                    && data.getMonthValue() == mes) {
                total += movimento.getValorComSinal(); // já vem negativo
            }
        }
        return Math.abs(total);
    }
}