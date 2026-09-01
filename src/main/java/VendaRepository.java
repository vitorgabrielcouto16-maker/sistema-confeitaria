import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaRepository {
    public void salvar(Venda venda) throws SQLException {

        String sql = "INSERT INTO vendas (dataVenda, clienteId) VALUES (?, ?)";

        try (Connection conn = ConexaoBanco.getConexao()) {
            conn.setAutoCommit(false);

            try {
                PreparedStatement stmtVenda = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmtVenda.setString(1, venda.getDataVenda().toString());
                stmtVenda.setInt(2, venda.getClienteId());
                stmtVenda.executeUpdate();
                ResultSet keys = stmtVenda.getGeneratedKeys();
                int vendaId = 0;
                if (keys.next()) {
                    vendaId = keys.getInt(1);
                }
                String sqlItem = "INSERT INTO itens_venda (produtoId, vendaId, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
                for (ItemVenda item : venda.getItens()) {
                    PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                    stmtItem.setInt(1, item.getProdutoId());
                    stmtItem.setInt(2, vendaId);
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.executeUpdate();

                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private List<ItemVenda> buscarItensDaVenda(Connection conn, int vendaId) throws SQLException {
        String sql = "SELECT * FROM itens_venda WHERE vendaId = ?";
        List<ItemVenda> itens = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenda itemVenda = new ItemVenda(
                            rs.getInt("produtoId"),
                            rs.getInt("quantidade"),
                            rs.getDouble("precoUnitario"),
                            rs.getInt("id")
                    );
                    itens.add(itemVenda);
                }
            }
        }
        return itens;
    }

    public List<Venda> listarTodos() throws SQLException {
        String sql = "SELECT * FROM vendas";
        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate data = LocalDate.parse(rs.getString("dataVenda"));
                List<ItemVenda> itens = buscarItensDaVenda(conn, rs.getInt("id"));

                Venda venda = new Venda(
                        rs.getInt("id"),
                        data,
                        rs.getInt("clienteId"),
                        itens
                );
                vendas.add(venda);

            }
            return vendas;
        }
    }

}
