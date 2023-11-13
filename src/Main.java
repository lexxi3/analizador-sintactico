
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String rutaArchivoEntrada = ".\\codigo.txt";
        String rutaTokens = ".\\tokens.csv";

        Lexico lexico = new Lexico(rutaArchivoEntrada);
        lexico.analizar();

        Tokens tokens = new Tokens(rutaTokens);
        List<Token> tokenList = tokens.obtenerTokens();

        Sintactico sintactico = new Sintactico(tokenList);
        sintactico.analizar();
    }
}
