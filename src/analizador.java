import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import com.opencsv.CSVWriter;
import java.util.HashMap;
import java.util.Map;

public class analizador {
    public static void main(String[] args) {
        try {
            // Abrir el archivo de entrada para lectura
            FileReader archivo = new FileReader("C:\\Users\\admin\\Documents\\LyA\\src\\entrada.txt");
            BufferedReader lector = new BufferedReader(archivo);

            // Crear una lista para almacenar los tokens
            ArrayList<String[]> tokens = new ArrayList<String[]>();

            // Crear una lista para almacenar los errores léxicos
            ArrayList<String[]> erroresLexicos = new ArrayList<String[]>();

            int numeroLinea = 1;
            String linea;

            // Expresiones regulares para cada categoría léxica
            String identificador = "[a-zA-Z_][a-zA-Z_0-9]*[$%&@?]";
            String operadoresAritmeticos = "[-+*/=]";
            String operadoresRelacionales = "[<>]=?|==|!=";
            String operadoresLogicos = "&&|\\|\\||!";
            String palabrasReservadas = "inicio|fin|leer|escribir|entero|real|cadena|logico|si|sino|entonces|mientras|hacer|repetir|hasta|variables";
            String caracteresEspeciales = "[();,:]";
            String comentarios = "//[^\\n]*";
            String numeroEntero = "[0-9]+";
            String numeroReal = "[0-9]+\\.[0-9]+";
            String constanteString = "\"[^\"]*\"";
            String valorLogico = "verdadero|falso";

            // Patrón para reconocer cualquier token
            String patron = String.format("(%s|%s|%s|%s|%s|%s|%s|%s|%s|%s)",
                    identificador, operadoresAritmeticos, operadoresRelacionales,
                    operadoresLogicos, palabrasReservadas, caracteresEspeciales,
                    comentarios, numeroEntero, numeroReal, constanteString, valorLogico);

            Pattern pattern = Pattern.compile(patron);

            // Define un mapa para asignar categorías numéricas a cada categoría léxica
            Map<String, Integer> categorias = new HashMap<String, Integer>();
            categorias.put("IDENTIFICADOR", -1);
            categorias.put("PALABRA_RESERVADA", -2);
            categorias.put("OPERADOR_ARITMETICO", -3);
            categorias.put("OPERADOR_RELACIONAL", -4);
            categorias.put("OPERADOR_LOGICO", -5);
            categorias.put("CARACTER_ESPECIAL", -6);
            categorias.put("COMENTARIO", -7);
            categorias.put("NUMERO_ENTERO", -8);
            categorias.put("NUMERO_REAL", -9);
            categorias.put("CONSTANTE_STRING", -10);
            categorias.put("VALOR_LOGICO", -11);

            while ((linea = lector.readLine()) != null) {
                Matcher matcher = pattern.matcher(linea);
                int posicion = 0;

                while (matcher.find(posicion)) {
                    String lexema = matcher.group();
                    String token = "";
                    int categoria = 0; // Por defecto, categoría 0 (desconocido)

                    if (Pattern.matches(identificador, lexema)) {
                        token = "IDENTIFICADOR";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(operadoresAritmeticos, lexema)) {
                        token = "OPERADOR_ARITMETICO";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(operadoresRelacionales, lexema)) {
                        token = "OPERADOR_RELACIONAL";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(operadoresLogicos, lexema)) {
                        token = "OPERADOR_LOGICO";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(palabrasReservadas, lexema)) {
                        token = "PALABRA_RESERVADA";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(caracteresEspeciales, lexema)) {
                        token = "CARACTER_ESPECIAL";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(comentarios, lexema)) {
                        token = "COMENTARIO";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(numeroEntero, lexema)) {
                        token = "NUMERO_ENTERO";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(numeroReal, lexema)) {
                        token = "NUMERO_REAL";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(constanteString, lexema)) {
                        token = "CONSTANTE_STRING";
                        categoria = categorias.get(token);
                    } else if (Pattern.matches(valorLogico, lexema)) {
                        token = "VALOR_LOGICO";
                        categoria = categorias.get(token);
                    } else {
                        // Si no coincide con ninguna expresión regular, es un error léxico
                        token = "ERROR_LEXICO";
                        categoria = categorias.get(token);
                        String[] error = { lexema, token, Integer.toString(numeroLinea) };
                        erroresLexicos.add(error);
                    }

                    // Si no es un error léxico, agrega el lexema, token, número de línea y
                    // categoría a la lista de tokens
                    if (!token.equals("ERROR_LEXICO")) {
                        String[] fila = { lexema, token, Integer.toString(numeroLinea), Integer.toString(categoria) };
                        tokens.add(fila);
                    }

                    posicion = matcher.end();
                }

                numeroLinea++;
            }

            // Cerrar el archivo de entrada
            lector.close();

            // Crear un archivo CSV para escribir los tokens
            FileWriter archivoSalida = new FileWriter("tokens.csv");
            CSVWriter csvWriter = new CSVWriter(archivoSalida);

            // Escribir los encabezados en el archivo CSV
            String[] encabezados = { "Lexema", "Token", "Número de línea", "Categoría" };
            csvWriter.writeNext(encabezados);

            // Escribir los datos de tokens en el archivo CSV
            csvWriter.writeAll(tokens);

            // Cerrar el archivo CSV de tokens
            csvWriter.close();

            // Crear un archivo CSV para escribir los errores léxicos
            FileWriter erroresSalida = new FileWriter("errores_lexicos.csv");
            CSVWriter erroresCSVWriter = new CSVWriter(erroresSalida);

            // Escribir los encabezados en el archivo de errores léxicos
            String[] erroresEncabezados = { "Lexema", "Tipo de Error", "Número de línea" };
            erroresCSVWriter.writeNext(erroresEncabezados);

            // Escribir los datos de errores léxicos en el archivo de errores léxicos
            erroresCSVWriter.writeAll(erroresLexicos);

            // Cerrar el archivo CSV de errores léxicos
            erroresCSVWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
