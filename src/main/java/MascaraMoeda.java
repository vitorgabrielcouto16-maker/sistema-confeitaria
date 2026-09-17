import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MascaraMoeda {

    private static final DecimalFormatSymbols SIMBOLOS_BR = new DecimalFormatSymbols(Locale.of("pt", "BR"));
    private static final DecimalFormat FORMATO_BR = new DecimalFormat("#,##0.00", SIMBOLOS_BR);

    public static void aplicar(TextField campo) {
        campo.textProperty().addListener((obs, valorAntigo, valorNovo) -> {
            String apenasDigitos = valorNovo.replaceAll("[^0-9]", "");

            if (apenasDigitos.isEmpty()) {
                campo.setText("");
                return;
            }

            BigDecimal valor = new BigDecimal(apenasDigitos).movePointLeft(2);
            String formatado = FORMATO_BR.format(valor);

            campo.setText(formatado);
            campo.positionCaret(formatado.length());
        });
    }

    public static double paraDouble(String textoFormatado) {
        String limpo = textoFormatado.replace(".", "").replace(",", ".");
        return limpo.isEmpty() ? 0.0 : Double.parseDouble(limpo);
    }
}
