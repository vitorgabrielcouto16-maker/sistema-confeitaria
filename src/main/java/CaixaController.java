import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class CaixaController {

    @FXML private Label labelSaldo;
    @FXML private Label labelEntradasMes;
    @FXML private Label labelSaidasMes;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField campoValor;
    @FXML private TextField campoDescricao;

    @FXML private TableView<MovimentoCaixa> tabelaMovimentos;
    @FXML private TableColumn<MovimentoCaixa, String> colunaData;
    @FXML private TableColumn<MovimentoCaixa, MovimentoCaixa> colunaTipo;
    @FXML private TableColumn<MovimentoCaixa, String> colunaDescricao;
    @FXML private TableColumn<MovimentoCaixa, Double> colunaValor;

    private final MovimentoCaixaRepository movimentoCaixaRepository = new MovimentoCaixaRepository();
    private final RelatorioCaixa relatorioCaixa = new RelatorioCaixa(movimentoCaixaRepository);

    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        comboTipo.setValue("Entrada");

        colunaData.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getData().toString()));

        colunaTipo.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(cell.getValue()));
        colunaTipo.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(MovimentoCaixa movimento, boolean vazio) {
                super.updateItem(movimento, vazio);
                if (vazio || movimento == null) {
                    setGraphic(null);
                    return;
                }
                boolean isEntrada = movimento instanceof Entrada;
                Label badge = new Label(isEntrada ? "Entrada" : "Saída");
                badge.getStyleClass().addAll("badge", isEntrada ? "badge-entrada" : "badge-saida");
                setGraphic(badge);
                setAlignment(Pos.CENTER_LEFT);
            }
        });

        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaValor.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getValorComSinal()).asObject());

        carregarMovimentos();
        atualizarSaldo();
        atualizarResumoMes();
    }

    private void carregarMovimentos() {
        try {
            List<MovimentoCaixa> movimentos = movimentoCaixaRepository.listarTodos();
            tabelaMovimentos.setItems(FXCollections.observableArrayList(movimentos));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar movimentos: " + e.getMessage());
        }
    }

    private void atualizarSaldo() {
        try {
            double saldo = relatorioCaixa.calcularSaldo();
            labelSaldo.setText(formatar(saldo));
        } catch (SQLException e) {
            labelSaldo.setText("Erro");
        }
    }

    private void atualizarResumoMes() {
        try {
            LocalDate hoje = LocalDate.now();
            double entradas = relatorioCaixa.calcularTotalEntradas(hoje.getYear(), hoje.getMonthValue());
            double saidas = relatorioCaixa.calcularTotalSaidas(hoje.getYear(), hoje.getMonthValue());
            labelEntradasMes.setText(formatar(entradas));
            labelSaidasMes.setText(formatar(saidas));
        } catch (SQLException e) {
            labelEntradasMes.setText("Erro");
            labelSaidasMes.setText("Erro");
        }
    }

    private String formatar(double valor) {
        return String.format(Locale.of("pt", "BR"), "R$ %.2f", valor);
    }

    @FXML
    private void lancarMovimento() {
        try {
            double valor = Double.parseDouble(campoValor.getText());
            if (valor <= 0) {
                mostrarErro("O valor deve ser maior que zero.");
                return;
            }
            String descricao = campoDescricao.getText();

            MovimentoCaixa movimento;
            if (comboTipo.getValue().equals("Entrada")) {
                movimento = new Entrada(LocalDate.now(), valor, descricao);
            } else {
                movimento = new Saida(LocalDate.now(), valor, descricao);
            }

            movimentoCaixaRepository.salvar(movimento);

            campoValor.clear();
            campoDescricao.clear();

            carregarMovimentos();
            atualizarSaldo();
            atualizarResumoMes();
        } catch (NumberFormatException e) {
            mostrarErro("Valor inválido. Digite um número, ex: 25.50");
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar movimento: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
}