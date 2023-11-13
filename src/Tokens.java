import java.util.*;
import java.io.*;

public class Tokens {

    private String rutaArchivoEntrada;

    public Tokens(String rutaArchivoEntrada) {
        this.rutaArchivoEntrada = rutaArchivoEntrada;
    }

    public List<Token> obtenerTokens() {
        List<Token> tokenList = new ArrayList<>();

        try {
            File file = new File(rutaArchivoEntrada); // Reemplaza "ruta_del_archivo.csv" con la ruta real de tu
                                                      // archivo CSV
            Scanner scanner = new Scanner(file);

            // Saltar la primera línea que contiene los encabezados
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Asumiendo que las columnas son Lexema, Token, Número de línea, Categoría
                String token = parts[0].replaceAll("\"", "");
                String numeroLinea = parts[1].replaceAll("\"", "");
                String categoria = parts[2].replaceAll("\"", "");
                String nombre = parts[3].replaceAll("\"", "");

                Token currentToken = new Token(token, numeroLinea, categoria, nombre);
                tokenList.add(currentToken);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return tokenList;
    }

}
