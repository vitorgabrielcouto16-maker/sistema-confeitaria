import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VendasController {

    @FXML private ComboBox<Cliente> comboCliente;
    @FXML private ComboBox<Produto> comboProduto;
    @FXML private TextField campoQuantidade;
    @FXML private Label labelTotal;

    @FXML private TableView<ItemCarrinho> tabelaCarrinho;
    @FXML private TableColumn<ItemCarrinho, String> colunaProdutoCarrinho;
    @FXML private TableColumn<ItemCarrinho, Integer> colunaQuantidadeCarrinho;
    @FXML private TableColumn<ItemCarrinho, Double> colunaPrecoCarrinho;
    @FXML private TableColumn<ItemCarrinho, Double> colunaSubtotalCarrinho;

    @FXML private TableView<Venda> tabelaHistorico;
    @FXML private TableColumn<Venda, Integer> colunaIdVenda;
    @FXML private TableColumn<Venda, String> colunaDataVenda;
    @FXML private TableColumn<Venda, String> colunaClienteVenda;
    @FXML private TableColumn<Venda, Double> colunaTotalVenda;

    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final VendaRepository vendaRepository = new VendaRepository();

    private final ObservableList<ItemCarrinho> carrinho = FXCollections.observableArrayList();
    private final Map<Integer, String> nomesClientes = new HashMap<>();

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabelaCarrinho();
        configurarTabelaHistorico();
        carregarCombos();
        carregarHistorico();
    }

    private void configurarCombos() {
        StringConverter<Cliente> conversorCliente = new StringConverter<>() {
            @Override
            public String toString(Cliente c) {
                return c == null ? "" : c.getNome();
            }
            @Override
            public Cliente fromString(String s) {
                return null;
            }
        };
        comboCliente.setConverter(conversorCliente);

        StringConverter<Produto> conversorProduto = new StringConverter<>() {
            @Override
            public String toString(Produto p) {
                return p == null ? "" : p.getNome() + " (R$ " + p.getPreco() + ")";
            }
            @Override
            public Produto fromString(String s) {
                return null;
            }
        };
        comboProduto.setConverter(conversorProduto);
    }

    private void configurarTabelaCarrinho() {
        colunaProdutoCarrinho.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colunaQuantidadeCarrinho.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPrecoCarrinho.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getProduto().getPreco()).asObject());
        colunaSubtotalCarrinho.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getSubtotal()).asObject());
        tabelaCarrinho.setItems(carrinho);
    }

    private void configurarTabelaHistorico() {
        colunaIdVenda.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        colunaDataVenda.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getDataVenda().toString()));
        colunaClienteVenda.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        nomesClientes.getOrDefault(cell.getValue().getClienteId(), "Cliente #" + cell.getValue().getClienteId())));
        colunaTotalVenda.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotal()).asObject());
    }

    private void carregarCombos() {
        try {
            List<Cliente> clientes = clienteRepository.listarTodos();
            comboCliente.setItems(FXCollections.observableArrayList(clientes));
            nomesClientes.clear();
            for (Cliente c : clientes) {
                nomesClientes.put(c.getIdCliente(), c.getNome());
            }

            List<Produto> produtos = produtoRepository.listarTodos();
            comboProduto.setItems(FXCollections.observableArrayList(produtos));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void carregarHistorico() {
        try {
            tabelaHistorico.setItems(FXCollections.observableArrayList(vendaRepository.listarTodos()));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar histórico: " + e.getMessage());
        }
    }

    @FXML
    private void adicionarItem() {
        Produto produto = comboProduto.getValue();
        if (produto == null) {
            mostrarErro("Selecione um produto.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText());
            if (quantidade <= 0) {
                mostrarErro("A quantidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Quantidade inválida.");
            return;
        }

        carrinho.add(new ItemCarrinho(produto, quantidade));
        campoQuantidade.clear();
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = 0;
        for (ItemCarrinho item : carrinho) {
            total += item.getSubtotal();
        }
        labelTotal.setText(String.format(Locale.of("pt", "BR"), "Total: R$ %.2f", total));
    }

    @FXML
    private void finalizarVenda() {
        Cliente cliente = comboCliente.getValue();
        if (cliente == null) {
            mostrarErro("Selecione um cliente.");
            return;
        }
        if (carrinho.isEmpty()) {
            mostrarErro("Adicione ao menos um item à venda.");
            return;
        }

        try {
            List<ItemVenda> itens = new ArrayList<>();
            for (ItemCarrinho item : carrinho) {
                itens.add(new ItemVenda(item.getQuantidade(), item.getProduto().getPreco(), item.getProduto().getId()));
            }

            Venda venda = new Venda(LocalDate.now(), cliente.getIdCliente(), itens);
            vendaRepository.salvar(venda);

            Entrada entradaCaixa = new Entrada(
                    LocalDate.now(),
                    venda.getTotal(),
                    "Venda para " + cliente.getNome()
            );
            movimentoCaixaRepository.salvar(entradaCaixa);

            carrinho.clear();
            atualizarTotal();
            carregarHistorico();
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar venda: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
    private final MovimentoCaixaRepository movimentoCaixaRepository = new MovimentoCaixaRepository();
}