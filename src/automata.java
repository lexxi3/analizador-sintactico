import javax.swing.JOptionPane;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class automata {
    public static void main(String[] args) {
        // Obtiene las cadenas de entrada desde el usuario utilizando
        String input = obtenerEntradaDesdeJOptionPane();

        // Divide las cadenas en palabras individuales utilizando espacios como
        // delimitador
        String[] cadenas = input.split("\\s+");

        // Inicializa una cadena para almacenar los resultados
        String resultado = "Cadena, Componente lexico\n";

        // Itera a través de las cadenas y las clasifica
        for (String cadena : cadenas) {
            String componenteLexico = clasificarCadena(cadena);

            // Agrega la cadena y su componente léxico al resultado
            resultado += cadena + "," + componenteLexico + "\n";
        }

        // Muestra los resultados en una ventana emergente
        guardarResultado(resultado);
    }

    // Función para clasificar una cadena en un componente léxico
    public static String clasificarCadena(String cadena) {
        if (esIdentificador(cadena)) {
            return "Identificador";
        } else if (esComentario(cadena)) {
            return "Comentario";
        } else if (esConstanteString(cadena)) {
            return "Constante String";
        } else {
            return "No identificado";
        }
    }

    // Función para verificar si una cadena es un identificador
    public static boolean esIdentificador(String cadena) {
        Pattern patron = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
        Matcher matcher = patron.matcher(cadena);
        return matcher.matches();
    }

    // Función para verificar si una cadena es un comentario
    public static boolean esComentario(String cadena) {
        return cadena.startsWith("//");
    }

    // Función para verificar si una cadena es una constante String
    public static boolean esConstanteString(String cadena) {
        // Verificar si la cadena comienza y termina con comillas dobles
        if (cadena.startsWith("\"") && cadena.endsWith("\"")) {
            // Además, asegurarse de que haya algo entre las comillas (longitud mayor a 2)
            return cadena.length() > 2;
        }
        return false;
    }

    // Función para obtener las cadenas de entrada
    public static String obtenerEntradaDesdeJOptionPane() {
        return JOptionPane.showInputDialog("Ingrese las cadenas separadas por espacios:");
    }

    // Función para mostrar los resultados en una ventana emergente
    public static void guardarResultado(String resultado) {
        JOptionPane.showMessageDialog(null, resultado, "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }

    // Funcion para almacenar en un archivo externo
    public static void guardarResultadoEnArchivo(String resultado) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resultados.txt"))) {
            bw.write(resultado);
            System.out.println("Resultados guardados en 'resultados.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
