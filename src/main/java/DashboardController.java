import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.Locale;

public class DashboardController {

    @FXML private Label labelSaldo;
    @FXML private Label labelQuantidadeProdutos;
    @FXML private Label labelQuantidadeClientes;

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final RelatorioCaixa relatorioCaixa = new RelatorioCaixa(new MovimentoCaixaRepository());

    @FXML
    public void initialize() {
        try {
            double saldo = relatorioCaixa.calcularSaldo();
            labelSaldo.setText(String.format(Locale.of("pt", "BR"), "R$ %.2f", saldo));

            int totalProdutos = produtoRepository.listarTodos().size();
            labelQuantidadeProdutos.setText(String.valueOf(totalProdutos));

            int totalClientes = clienteRepository.listarTodos().size();
            labelQuantidadeClientes.setText(String.valueOf(totalClientes));
        } catch (SQLException e) {
            labelSaldo.setText("Erro");
        }
    }
}