import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentoCaixaRepository {
    public void salvar(MovimentoCaixa movimento) throws SQLException {

        String sql = "INSERT INTO movimentocaixa(data, valor,descricao,tipo) VALUES (?,?,?,?)";

        String tipo;
        if (movimento instanceof Entrada) {
            tipo = "entrada";
        } else {
            tipo = "saida";
        }

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movimento.getData().toString());
            stmt.setDouble(2, movimento.getValor());
            stmt.setString(3, movimento.getDescricao());
            stmt.setString(4, tipo);
            stmt.executeUpdate();

        }
    }

    public List<MovimentoCaixa> listarTodos() throws SQLException {
        String sql = "SELECT * FROM movimentoCaixa";
        List<MovimentoCaixa> movimentos = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");

                if (tipo.equals("entrada")) {
                    Entrada entrada = new Entrada(
                            rs.getInt("id"),
                            LocalDate.parse(rs.getString("data")),
                            rs.getDouble("valor"),
                            rs.getString("descricao")

                    );
                    movimentos.add(entrada);
                }else {
                    Saida saida = new Saida(
                            rs.getInt("id"),
                            LocalDate.parse(rs.getString("data")),
                            rs.getDouble("valor"),
                            rs.getString("descricao")
                    );
                    movimentos.add(saida);
                }
            }
        }
        return movimentos;
    }
}

