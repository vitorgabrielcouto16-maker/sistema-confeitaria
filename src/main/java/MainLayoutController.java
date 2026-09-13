import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainLayoutController {

    @FXML private StackPane areaConteudo;
    @FXML private Button btnDashboard;
    @FXML private Button btnProdutos;
    @FXML private Button btnClientes;
    @FXML private Button btnVendas;
    @FXML private Button btnCaixa;

    @FXML
    public void initialize() {
        abrirDashboard();
    }

    @FXML
    private void abrirDashboard() {
        carregarTela("/dashboard.fxml", btnDashboard);
    }

    @FXML
    private void abrirProdutos() {
        carregarTela("/produtos.fxml", btnProdutos);
    }

    @FXML
    private void abrirClientes() {
        carregarTela("/clientes.fxml", btnClientes);
    }

    @FXML
    private void abrirVendas() {
        carregarTela("/vendas.fxml", btnVendas);
    }

    @FXML
    private void abrirCaixa() {
        carregarTela("/caixa.fxml", btnCaixa);
    }

    private void carregarTela(String caminhoFxml, Button botaoAtivo) {
        try {
            Parent tela = FXMLLoader.load(getClass().getResource(caminhoFxml));
            areaConteudo.getChildren().setAll(tela);
            marcarBotaoAtivo(botaoAtivo);
        } catch (IOException e) {
            areaConteudo.getChildren().setAll(new Label("Erro ao carregar tela: " + e.getMessage()));
        }
    }

    private void mostrarEmConstrucao(String nomeTela, Button botaoAtivo) {
        Label label = new Label(nomeTela + " - Em construção");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #999999;");
        areaConteudo.getChildren().setAll(label);
        marcarBotaoAtivo(botaoAtivo);
    }

    private void marcarBotaoAtivo(Button botaoAtivo) {
        for (Button b : new Button[]{btnDashboard, btnProdutos, btnClientes, btnVendas, btnCaixa}) {
            b.getStyleClass().remove("nav-button-selected");
            if (!b.getStyleClass().contains("nav-button")) {
                b.getStyleClass().add("nav-button");
            }
        }
        botaoAtivo.getStyleClass().remove("nav-button");
        botaoAtivo.getStyleClass().add("nav-button-selected");
    }
}