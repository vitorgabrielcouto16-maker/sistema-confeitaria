import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class ConexaoBanco {
    private static final String URL;

    static {
        String pasta = System.getProperty("user.home") + File.separator + "AppConfeitaria";
        new File(pasta).mkdirs();
        URL = "jdbc:sqlite:" + pasta + File.separator + "confeitaria.db";
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}