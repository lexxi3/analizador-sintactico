import javax.swing.JOptionPane;
import java.io.*;
import java.util.Scanner;

public class Menu {

    public static void main(String args[]) {

        String texto = "";
        int opcion, totalOpciones = 4;

        String menu = "             Automatas \n";
        menu += "1.  Leer texto de entrada \n";
        menu += "2.  Guardar texto \n";
        menu += "3.  Leer texto del archivo \n";
        menu += "4.  Terminar \n";

        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(menu));

            switch (opcion) {
                case 1:
                    texto = JOptionPane.showInputDialog("Ingrese el texto");
                    break;

                case 2:
                    String nombre = JOptionPane.showInputDialog("Ingresa el nombre del archivo");
                    guardarTextoEnArchivo(nombre, texto);
                    break;

                case 3:
                    String nombreArchivo = JOptionPane.showInputDialog("Ingresa el nombre del archivo");
                    leerTextoDeArchivo(nombreArchivo);
                    break;

                case 4:
                    JOptionPane.showMessageDialog(null, "Programa terminado");

                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida");
                    break;

            }
        } while (opcion != totalOpciones);

    }

    private static void guardarTextoEnArchivo(String nombreArchivo, String texto) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
            writer.write(texto);
            writer.close();
            JOptionPane.showMessageDialog(null, "Texto guardado en " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo");
        }
    }

    // Método para leer el texto desde un archivo
    private static void leerTextoDeArchivo(String nombreArchivo) {
        try {
            Scanner scanner = new Scanner(new File(nombreArchivo));
            StringBuilder contenido = new StringBuilder();
            int caracteres = 0;
            int palabras = 0;
            int lineas = 0;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                contenido.append(linea).append("\n");
                caracteres += linea.length();
                palabras += linea.split("\\s+").length;
                lineas++;
            }

            scanner.close();

            JOptionPane.showMessageDialog(null, "Texto leído del archivo:\n" + contenido.toString() +
                    "\n\nCaracteres totales: " + caracteres +
                    "\nPalabras totales: " + palabras +
                    "\nLíneas totales: " + lineas);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
    }

}
