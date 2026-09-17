import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ProdutoFormController {

    @FXML private TextField campoNome;
    @FXML private TextField campoPreco;
    @FXML private TextField campoDescricao;
    @FXML private CheckBox campoAtivo;

    @FXML
    public void initialize() {
        MascaraMoeda.aplicar(campoPreco);
    }

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    @FXML
    private void salvar() {
        try {
            String nome = campoNome.getText();
            if (nome.isBlank()) {
                mostrarErro("Informe o nome do produto.");
                return;
            }
            double preco = MascaraMoeda.paraDouble(campoPreco.getText());
            String descricao = campoDescricao.getText();
            boolean ativo = campoAtivo.isSelected();

            Produto produto = new Produto(nome, preco, ativo, descricao);
            produtoRepository.salvar(produto);

            if (aoSalvar != null) {
                aoSalvar.run();
            }
            fechar();
        } catch (NumberFormatException e) {
            mostrarErro("Preço inválido. Digite um número, ex: 12.50");
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar produto: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }
}