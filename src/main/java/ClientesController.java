import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;


import java.sql.SQLException;

public class ClientesController {

    @FXML private TextField campoNome;
    @FXML private TextField campoTelefone;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Integer> colunaId;
    @FXML private TableColumn<Cliente, String> colunaNome;
    @FXML private TableColumn<Cliente, String> colunaTelefone;
    @FXML private TableColumn<Cliente, Void> colunaAcao;

    private final ClienteRepository clienteRepository = new ClienteRepository();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaAcao.setCellFactory(coluna -> new TableCell<>() {
            private final Button botaoExcluir = new Button("Excluir");
            private final HBox caixaBotoes = new HBox(5, botaoExcluir);

            {
                botaoExcluir.getStyleClass().add("btn-danger");

                botaoExcluir.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    apagarCliente(cliente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                setGraphic(vazio ? null : caixaBotoes);
            }
        });

        carregarClientes();
    }

    private void carregarClientes() {
        try {
            ObservableList<Cliente> lista = FXCollections.observableArrayList(clienteRepository.listarTodos());
            tabelaClientes.setItems(lista);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    @FXML
    private void salvarCliente() {
        try {
            String nome = campoNome.getText();
            String telefone = campoTelefone.getText();

            if (nome.isBlank()) {
                mostrarErro("Informe o nome do cliente.");
                return;
            }

            Cliente cliente = new Cliente(nome, telefone);
            clienteRepository.salvar(cliente);

            campoNome.clear();
            campoTelefone.clear();

            carregarClientes();
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
    private void apagarCliente(Cliente cliente) {
        try {
            clienteRepository.apagarCliente(cliente.getIdCliente());
            carregarClientes();
        } catch (SQLException e) {
            mostrarErro("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}