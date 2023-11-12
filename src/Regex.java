import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Regex {
    public static void main(String[] args) throws IOException {
        String[] options = { "Leer texto de la consola", "Leer archivo de texto", "Salir" };

        while (true) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una opción:",
                    "Analizador Léxico",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    analyzeTextFromConsole();
                    break;
                case 1:
                    analyzeTextFromFile();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Programa finalizado.");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
                    break;
            }
        }
    }

    private static void analyzeTextFromConsole() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder text = new StringBuilder();
        String line;
        int lineCount = 0;

        JOptionPane.showMessageDialog(null,
                "Ingrese texto desde la consola. Presione Enter dos veces para finalizar la entrada y da clic en OK.");

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (lineCount >= 2) {
                    break;
                }
                lineCount++;
            } else {
                text.append(line).append("\n");
                lineCount = 0;
            }
        }

        analyzeText(text.toString());
    }

    private static void analyzeTextFromFile() throws IOException {
        String filePath = JOptionPane
                .showInputDialog("Ingresa el nombre del archivo txt ubicado en el mismo nivel que el archivo de java:");
        StringBuilder text = new StringBuilder();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo: " + e.getMessage());
            return;
        }

        analyzeText(text.toString());
    }

    private static void analyzeText(String text) {
        String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String phoneRegex = "\\+\\d{10,15}";
        String dateRegex = "\\d{2}/\\d{2}/\\d{4}";

        Pattern emailPattern = Pattern.compile(emailRegex);
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Pattern datePattern = Pattern.compile(dateRegex);

        String[] lines = text.split("\n");

        StringBuilder result = new StringBuilder("Análisis léxico:\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            Matcher emailMatcher = emailPattern.matcher(line);
            Matcher phoneMatcher = phonePattern.matcher(line);
            Matcher dateMatcher = datePattern.matcher(line);

            while (emailMatcher.find()) {
                result.append("Línea ").append(i + 1).append(", Email: ").append(emailMatcher.group()).append("\n");
            }

            while (phoneMatcher.find()) {
                result.append("Línea ").append(i + 1).append(", Teléfono: ").append(phoneMatcher.group()).append("\n");
            }

            while (dateMatcher.find()) {
                result.append("Línea ").append(i + 1).append(", Fecha: ").append(dateMatcher.group()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(null, result.toString());
    }
}