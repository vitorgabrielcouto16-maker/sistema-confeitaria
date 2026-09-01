import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    public void salvar (Produto produto) throws SQLException {

        String sql = "INSERT INTO produtos (nome, preco, ativoParaVenda, descricaoProduto) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setBoolean(3, produto.isAtivoParaVenda());
            stmt.setString(4, produto.getDescricaoProduto());
            stmt.executeUpdate();
        }


    }
    public List<Produto> listarTodos() throws SQLException{
        String sql = "SELECT * FROM produtos";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConexao();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()){
                Produto produto = new Produto(
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getBoolean("ativoParaVenda"),
                rs.getString("descricaoProduto"),
                rs.getInt("id")
                );
                   produtos.add(produto);

            }
                return produtos;
        }
    }



}
