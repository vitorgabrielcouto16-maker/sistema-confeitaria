import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final String URL = "jdbc:sqlite:confeitaria.db";

    public static Connection getConexao() throws SQLException{
        return DriverManager.getConnection(URL);


    }
}
