import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label labelSaldo;
    @FXML private Label labelQuantidadeProdutos;
    @FXML private Label labelQuantidadeClientes;

    @FXML private Label labelFaturamentoMes;
    @FXML private Label labelVendasHoje;
    @FXML private Label labelDespesasMes;

    @FXML private BarChart<String, Number> graficoVendas;
    @FXML private VBox containerTopProdutos;

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final VendaRepository vendaRepository = new VendaRepository();
    private final RelatorioCaixa relatorioCaixa = new RelatorioCaixa(new MovimentoCaixaRepository());

    @FXML
    public void initialize() {
        try {
            double saldo = relatorioCaixa.calcularSaldo();
            labelSaldo.setText(formatarMoeda(saldo));

            int totalProdutos = produtoRepository.listarTodos().size();
            labelQuantidadeProdutos.setText(String.valueOf(totalProdutos));

            int totalClientes = clienteRepository.listarTodos().size();
            labelQuantidadeClientes.setText(String.valueOf(totalClientes));
        } catch (SQLException e) {
            labelSaldo.setText("Erro");
        }

        carregarNovosCards();
        carregarGraficoVendas();
        carregarTopProdutos();
    }

    private void carregarNovosCards() {
        try {
            LocalDate hoje = LocalDate.now();
            List<Venda> vendas = vendaRepository.listarTodos();

            double faturamentoMes = vendas.stream()
                    .filter(v -> v.getDataVenda().getYear() == hoje.getYear()
                            && v.getDataVenda().getMonthValue() == hoje.getMonthValue())
                    .mapToDouble(Venda::getTotal)
                    .sum();

            double vendasHoje = vendas.stream()
                    .filter(v -> v.getDataVenda().isEqual(hoje))
                    .mapToDouble(Venda::getTotal)
                    .sum();

            double despesasMes = relatorioCaixa.calcularTotalSaidas(hoje.getYear(), hoje.getMonthValue());

            labelFaturamentoMes.setText(formatarMoeda(faturamentoMes));
            labelVendasHoje.setText(formatarMoeda(vendasHoje));
            labelDespesasMes.setText(formatarMoeda(despesasMes));
        } catch (SQLException e) {
            labelFaturamentoMes.setText("Erro");
            labelVendasHoje.setText("Erro");
            labelDespesasMes.setText("Erro");
        }
    }

    private void carregarGraficoVendas() {
        try {
            List<Venda> vendas = vendaRepository.listarTodos();
            LocalDate hoje = LocalDate.now();
            LocalDate seteDiasAtras = hoje.minusDays(6);

            Map<LocalDate, Double> totalPorDia = vendas.stream()
                    .filter(v -> !v.getDataVenda().isBefore(seteDiasAtras))
                    .collect(Collectors.groupingBy(
                            Venda::getDataVenda,
                            Collectors.summingDouble(Venda::getTotal)
                    ));

            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName("Vendas");

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM");

            for (LocalDate dia = seteDiasAtras; !dia.isAfter(hoje); dia = dia.plusDays(1)) {
                double total = totalPorDia.getOrDefault(dia, 0.0);
                serie.getData().add(new XYChart.Data<>(dia.format(formatador), total));
            }

            graficoVendas.getData().add(serie);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar gráfico de vendas: " + e.getMessage());
        }
    }

    private void carregarTopProdutos() {
        try {
            List<Venda> vendas = vendaRepository.listarTodos();
            List<Produto> produtos = produtoRepository.listarTodos();

            Map<Integer, String> nomesProdutos = produtos.stream()
                    .collect(Collectors.toMap(Produto::getId, Produto::getNome));

            Map<Integer, Integer> quantidadePorProduto = new HashMap<>();
            for (Venda venda : vendas) {
                for (ItemVenda item : venda.getItens()) {
                    quantidadePorProduto.merge(item.getProdutoId(), item.getQuantidade(), Integer::sum);
                }
            }

            List<Map.Entry<Integer, Integer>> ranking = quantidadePorProduto.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toList());

            containerTopProdutos.getChildren().clear();

            if (ranking.isEmpty()) {
                Label vazio = new Label("Nenhuma venda registrada ainda.");
                vazio.getStyleClass().add("field-label");
                containerTopProdutos.getChildren().add(vazio);
                return;
            }

            int posicao = 1;
            for (Map.Entry<Integer, Integer> entry : ranking) {
                String nome = nomesProdutos.getOrDefault(entry.getKey(), "Produto #" + entry.getKey());

                HBox linha = new HBox(10);
                linha.setAlignment(Pos.CENTER_LEFT);

                Label labelPosicao = new Label("#" + posicao);
                labelPosicao.getStyleClass().addAll("badge", "badge-entrada");

                Label labelNome = new Label(nome);
                HBox.setHgrow(labelNome, Priority.ALWAYS);

                Label labelQtd = new Label(entry.getValue() + " unid.");
                labelQtd.getStyleClass().add("field-label");

                linha.getChildren().addAll(labelPosicao, labelNome, labelQtd);
                containerTopProdutos.getChildren().add(linha);
                posicao++;
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar top produtos: " + e.getMessage());
        }
    }

    private String formatarMoeda(double valor) {
        return String.format(Locale.of("pt", "BR"), "R$ %.2f", valor);
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
}
