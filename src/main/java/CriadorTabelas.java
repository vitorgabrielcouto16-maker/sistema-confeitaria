import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CriadorTabelas {
    public static void criarTabelas() throws SQLException {
        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS produtos(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT,
                    preco REAL,
                    ativoParaVenda INTEGER,
                    descricaoProduto TEXT
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT,
                    telefone TEXT
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS vendas(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    dataVenda TEXT,
                    clienteId INTEGER,
                    FOREIGN KEY (clienteId) REFERENCES clientes(id)
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS itens_venda(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    produtoId INTEGER,
                    vendaId INTEGER,
                    quantidade INTEGER,
                    precoUnitario REAL,
                    FOREIGN KEY (produtoId) REFERENCES produtos(id),
                    FOREIGN KEY (vendaId) REFERENCES vendas(id)
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS movimentoCaixa(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    data TEXT,
                    valor REAL,
                    descricao TEXT,
                    entrada TEXT
                )
                """);
        }
    }
}