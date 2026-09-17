import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class ProdutosController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, Integer> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private TableColumn<Produto, String> colunaDescricao;
    @FXML private TableColumn<Produto, Boolean> colunaAtivo;
    @FXML private TableColumn<Produto, Void> colunaAcao;

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoProduto"));
        colunaAtivo.setCellValueFactory(new PropertyValueFactory<>("ativoParaVenda"));
        colunaAcao.setCellFactory(coluna -> new TableCell<>() {
            private final Button botaoStatus = new Button();
            private final Button botaoExcluir = new Button("Excluir");
            private final HBox caixaBotoes = new HBox(5, botaoStatus, botaoExcluir);

            {
                botaoStatus.getStyleClass().add("btn-secondary");
                botaoExcluir.getStyleClass().add("btn-danger");

                botaoStatus.setOnAction(e -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    alternarStatus(produto);
                });

                botaoExcluir.setOnAction(e -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    apagarProduto(produto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio) {
                    setGraphic(null);
                    return;
                }
                Produto produto = getTableView().getItems().get(getIndex());
                botaoStatus.setText(produto.isAtivoParaVenda() ? "Desativar" : "Ativar");
                setGraphic(caixaBotoes);
            }
        });
        carregarProdutos();
    }

    private void carregarProdutos() {
        try {
            tabelaProdutos.setItems(FXCollections.observableArrayList(produtoRepository.listarTodos()));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }
    private void alternarStatus(Produto produto) {
        try {
            produtoRepository.atualizarStatus(produto.getId(), !produto.isAtivoParaVenda());
            carregarProdutos();
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar produto: " + e.getMessage());
        }
    }
    private void apagarProduto(Produto produto) {
        try {
            produtoRepository.apagarProduto(produto.getId());
            carregarProdutos();
        } catch (SQLException e) {
            mostrarErro("Erro ao excluir produto: " + e.getMessage());
        }
    }

    @FXML
    private void abrirFormularioNovoProduto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/produto-form.fxml"));
            Parent root = loader.load();

            ProdutoFormController controller = loader.getController();
            controller.setAoSalvar(this::carregarProdutos);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Novo Produto");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            mostrarErro("Erro ao excluir produto: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
}