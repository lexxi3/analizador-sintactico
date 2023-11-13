import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;

public class Lexico {
    private String rutaArchivoEntrada;

    public Lexico(String rutaArchivoEntrada) {
        this.rutaArchivoEntrada = rutaArchivoEntrada;
    }

    public void analizar() {
        try {
            // Abrir el archivo de entrada para lectura
            FileReader archivo = new FileReader(rutaArchivoEntrada);
            BufferedReader lector = new BufferedReader(archivo);
            // Crear una lista para almacenar los tokens
            ArrayList<String[]> tokens = new ArrayList<String[]>();

            // Crear una lista para almacenar los errores léxicos
            ArrayList<String[]> erroresLexicos = new ArrayList<String[]>();

            int numeroLinea = 1;
            String linea;

            // Expresiones regulares para cada categoría léxica
            String identificador = "[a-zA-Z_][a-zA-Z_0-9]*[$%&@?]";
            String identificadorInicio = "[a-zA-Z_][a-zA-Z_0-9]*[?]";
            String identificadorLogico = "[a-zA-Z_][a-zA-Z_0-9]*[@]";
            String identificadorEntero = "[a-zA-Z_][a-zA-Z_0-9]*[&]";
            String identificadorReal = "[a-zA-Z_][a-zA-Z_0-9]*[%]";
            String identificadorCadena = "[a-zA-Z_][a-zA-Z_0-9]*[$]";

            String operadoresAritmeticos = "[-+*/=]";
            String operadoresAritmeticosResta = "[-]";
            String operadoresAritmeticosSuma = "[+]";
            String operadoresAritmeticosMultiplicacion = "[*]";
            String operadoresAritmeticosDivision = "[/]";
            String operadoresAritmeticosAsignacion = "[=]";

            String operadoresRelacionales = "[<>]=?|==|!=";
            String operadoresRelacionalesMenorIgual = "<=";
            String operadoresRelacionalesMayorIgual = ">=";
            String operadoresRelacionalesMenor = "<";
            String operadoresRelacionalesMayor = ">";
            String operadoresRelacionalesIgual = "==";
            String operadoresRelacionalesDiferente = "!=";

            String operadoresLogicos = "&&|\\|\\||!";
            String operadoresLogicosAnd = "&&";
            String operadoresLogicosOr = "\\|\\|";
            String operadoresLogicosNot = "!";

            String palabrasReservadas = "programa|inicio|fin|leer|escribir|entero|real|cadena|logico|sino|si|entonces|mientras|hacer|repetir|hasta|variables";
            String programa = "programa";
            String inicio = "inicio";
            String fin = "fin";
            String leer = "leer";
            String escribir = "escribir";
            String entero = "entero";
            String real = "real";
            String cadena = "cadena";
            String logico = "logico";
            String si = "si";
            String sino = "sino";
            String entonces = "entonces";
            String mientras = "mientras";
            String hacer = "hacer";
            String repetir = "repetir";
            String hasta = "hasta";
            String variables = "variables";

            String numeroEntero = "[0-9]+";
            String numeroReal = "[0-9]+\\.[0-9]+";
            String constanteString = "\"[^\"]*\"";

            String valorLogico = "verdadero|falso";
            String verdadero = "verdadero";
            String falso = "falso";

            String caracteresEspeciales = "[();,:]";
            String dosPunto = ":";
            String coma = ",";
            String puntoComa = ";";
            String parentesisAbiertos = "[(]";
            String parentesisCerrado = "[)]";

            String comentarios = "//.*";

            // Patrón para reconocer cualquier token
            String patron = String.format("(%s|%s|%s|%s|%s|%s|%s|%s|%s|%s)",
                    comentarios, identificador, operadoresRelacionales, operadoresAritmeticos,
                    operadoresLogicos, palabrasReservadas, caracteresEspeciales, numeroReal,
                    numeroEntero, constanteString, valorLogico);

            Pattern pattern = Pattern.compile(patron);

            // Define un mapa para asignar categorías numéricas a cada categoría léxica
            Map<String, Map<String, Integer>> categorias = new HashMap<String, Map<String, Integer>>();

            Map<String, Integer> identificadoresMap = new HashMap<String, Integer>();
            identificadoresMap.put("IDENTIFICADOR_INICIO", -1);
            identificadoresMap.put("IDENTIFICADOR_LOGICO", -2);
            identificadoresMap.put("IDENTIFICADOR_ENTERO", -3);
            identificadoresMap.put("IDENTIFICADOR_REAL", -4);
            identificadoresMap.put("IDENTIFICADOR_STRING", -5);

            Map<String, Integer> palabrasMap = new HashMap<String, Integer>();
            palabrasMap.put("INICIO", -10);
            palabrasMap.put("FIN", -11);
            palabrasMap.put("LEER", -12);
            palabrasMap.put("ESCRIBIR", -13);
            palabrasMap.put("ENTERO", -14);
            palabrasMap.put("REAL", -15);
            palabrasMap.put("CADENA", -16);
            palabrasMap.put("LOGICO", -17);
            palabrasMap.put("SI", -18);
            palabrasMap.put("SINO", -19);
            palabrasMap.put("ENTONCES", -20);
            palabrasMap.put("MIENTRAS", -21);
            palabrasMap.put("HACER", -22);
            palabrasMap.put("REPETIR", -23);
            palabrasMap.put("HASTA", -24);
            palabrasMap.put("VARIABLES", -25);
            palabrasMap.put("PROGRAMA", -10);

            Map<String, Integer> aritmeticosMap = new HashMap<String, Integer>();
            aritmeticosMap.put("SUMA", -30);
            aritmeticosMap.put("RESTA", -31);
            aritmeticosMap.put("MULTIPLICACION", -32);
            aritmeticosMap.put("DIVISION", -33);
            aritmeticosMap.put("ASIGNACION", -34);

            Map<String, Integer> relacionalesMap = new HashMap<String, Integer>();
            relacionalesMap.put("MENOR_IGUAL", -40);
            relacionalesMap.put("MAYOR_IGUAL", -41);
            relacionalesMap.put("MENOR", -42);
            relacionalesMap.put("MAYOR", -43);
            relacionalesMap.put("IGUAL", -44);
            relacionalesMap.put("DIFERENTE", -45);

            Map<String, Integer> logicosMap = new HashMap<String, Integer>();
            logicosMap.put("AND", -50);
            logicosMap.put("OR", -51);
            logicosMap.put("NOT", -52);

            Map<String, Integer> especialesMap = new HashMap<String, Integer>();
            especialesMap.put("DOSPUNTOS", -60);
            especialesMap.put("COMA", -61);
            especialesMap.put("PUNTOCOMA", -62);
            especialesMap.put("PARENTESISABIERTO", -63);
            especialesMap.put("PARENTESISCERRADO", -64);

            Map<String, Integer> valorLogicoMap = new HashMap<String, Integer>();
            valorLogicoMap.put("VERDADERO", -80);
            valorLogicoMap.put("FALSO", -81);

            categorias.put("IDENTIFICADOR", identificadoresMap);
            categorias.put("PALABRA_RESERVADA", palabrasMap);
            categorias.put("OPERADOR_ARITMETICO", aritmeticosMap);
            categorias.put("OPERADOR_RELACIONAL", relacionalesMap);
            categorias.put("OPERADOR_LOGICO", logicosMap);
            categorias.put("CARACTER_ESPECIAL", especialesMap);
            categorias.put("COMENTARIO", new HashMap<String, Integer>());
            categorias.put("VALOR_LOGICO", valorLogicoMap);

            while ((linea = lector.readLine()) != null) {
                Matcher matcher = pattern.matcher(linea);
                int posicion = 0;

                while (matcher.find(posicion)) {
                    String lexema = matcher.group();
                    String nombreCategoria = "";
                    String token = "";
                    int categoria = 0; // Por defecto, categoría 0 (desconocido)

                    if (Pattern.matches(comentarios, lexema)) {
                        token = "COMENTARIO";
                    } else if (Pattern.matches(identificador, lexema)) {
                        nombreCategoria = "IDENTIFICADOR";
                        if (Pattern.matches(identificadorInicio, lexema)) {
                            token = "IDENTIFICADOR_INICIO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else {
                            token = "IDENTIFICADOR";
                            categoria = -1;
                        }

                    } else if (Pattern.matches(operadoresRelacionales, lexema)) {
                        nombreCategoria = "OPERADOR_RELACIONAL";

                        if (Pattern.matches(operadoresRelacionalesIgual, lexema)) {
                            token = "IGUAL";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresRelacionalesMayor, lexema)) {
                            token = "MAYOR";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresRelacionalesMenor, lexema)) {
                            token = "MENOR";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresRelacionalesMayorIgual, lexema)) {
                            token = "MAYOR_IGUAL";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresRelacionalesMenorIgual, lexema)) {
                            token = "MENOR_IGUAL";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresRelacionalesDiferente, lexema)) {
                            token = "DIFERENTE";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }

                    } else if (Pattern.matches(operadoresAritmeticos, lexema)) {
                        nombreCategoria = "OPERADOR_ARITMETICO";
                        if (Pattern.matches(operadoresAritmeticosAsignacion, lexema)) {
                            token = "ASIGNACION";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresAritmeticosDivision, lexema)) {
                            token = "DIVISION";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresAritmeticosSuma, lexema)) {
                            token = "SUMA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresAritmeticosResta, lexema)) {
                            token = "RESTA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresAritmeticosMultiplicacion, lexema)) {
                            token = "MULTIPLICACION";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }

                    } else if (Pattern.matches(operadoresLogicos, lexema)) {
                        nombreCategoria = "OPERADOR_LOGICO";
                        if (Pattern.matches(operadoresLogicosAnd, lexema)) {
                            token = "AND";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresLogicosOr, lexema)) {
                            token = "OR";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(operadoresLogicosNot, lexema)) {
                            token = "NOT";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }
                    } else if (Pattern.matches(palabrasReservadas, lexema)) {
                        nombreCategoria = "PALABRA_RESERVADA";
                        if (Pattern.matches(inicio, lexema)) {
                            token = "INICIO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(fin, lexema)) {
                            token = "FIN";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(escribir, lexema)) {
                            token = "ESCRIBIR";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(leer, lexema)) {
                            token = "LEER";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(sino, lexema)) {
                            token = "SINO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(si, lexema)) {
                            token = "SI";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(mientras, lexema)) {
                            token = "MIENTRAS";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(hacer, lexema)) {
                            token = "HACER";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(repetir, lexema)) {
                            token = "REPETIR";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(hasta, lexema)) {
                            token = "HASTA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(variables, lexema)) {
                            token = "VARIABLES";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(entonces, lexema)) {
                            token = "ENTONCES";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(entero, lexema)) {
                            token = "ENTERO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(real, lexema)) {
                            token = "REAL";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(cadena, lexema)) {
                            token = "CADENA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(logico, lexema)) {
                            token = "LOGICO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(programa, lexema)) {
                            token = "PROGRAMA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }
                    } else if (Pattern.matches(caracteresEspeciales, lexema)) {
                        nombreCategoria = "CARACTER_ESPECIAL";
                        if (Pattern.matches(dosPunto, lexema)) {
                            token = "DOSPUNTOS";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(coma, lexema)) {
                            token = "COMA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(puntoComa, lexema)) {
                            token = "PUNTOCOMA";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(parentesisAbiertos, lexema)) {
                            token = "PARENTESISABIERTO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(parentesisCerrado, lexema)) {
                            token = "PARENTESISCERRADO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }
                    } else if (Pattern.matches(numeroEntero, lexema)) {
                        token = "NUMERO_ENTERO";
                        nombreCategoria = "NUMERO_ENTERO";
                        categoria = -70;
                    } else if (Pattern.matches(numeroReal, lexema)) {
                        token = "NUMERO_REAL";
                        nombreCategoria = "NUMERO_REAL";
                        categoria = -71;
                    } else if (Pattern.matches(constanteString, lexema)) {
                        token = "CONSTANTE_STRING";
                        nombreCategoria = "CONSTANTE_STRING";
                        categoria = -72;
                    } else if (Pattern.matches(valorLogico, lexema)) {
                        nombreCategoria = "VALOR_LOGICO";
                        if (Pattern.matches(verdadero, lexema)) {
                            token = "VERDADERO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        } else if (Pattern.matches(falso, lexema)) {
                            token = "FALSO";
                            categoria = categorias.get(nombreCategoria).get(token);
                        }
                    } else {
                        // Si no coincide con ninguna expresión regular, es un error léxico
                        token = "ERROR_LEXICO";
                        categoria = categorias.get(token).get(lexema);
                        String[] error = { lexema, token, Integer.toString(numeroLinea) };
                        erroresLexicos.add(error);
                    }

                    // Si no es un error léxico, agrega el lexema, token, número de línea y
                    // categoría a la lista de tokens
                    if (token.equals("COMENTARIO")) {
                        System.out.println("Comentario: " + lexema);
                    } else if (!token.equals("ERROR_LEXICO")) {
                        String[] fila = { token, Integer.toString(numeroLinea), Integer.toString(categoria),
                                nombreCategoria };
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
            String[] encabezados = { "Token", "Número de línea", "Categoría", "Nombre Categoria" };
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
