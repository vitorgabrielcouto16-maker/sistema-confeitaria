import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    public void salvar (Cliente cliente) throws SQLException {

        String sql = "INSERT INTO clientes (nome, telefone) VALUES (?, ?)";

        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.executeUpdate();
        }


    }
    public List<Cliente> listarTodos() throws SQLException{
        String sql = "SELECT * FROM clientes";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()){
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getInt("id")
                );
                clientes.add(cliente);

            }
            return clientes;
        }
    }
}
