import java.util.List;

public class Sintactico {
    private List<Token> tokenList;
    private int currentIndex;

    public Sintactico(List<Token> tokenList) {
        this.tokenList = tokenList;
        this.currentIndex = 0;
    }

    public void analizar() {
        try {
            programa();
            System.out.println("Análisis sintáctico completo.");
        } catch (Exception e) {
            System.out.println("Error sintáctico: " + e.getMessage());
        }
    }

    // Estructura inicial del codigo
    private void programa() throws Exception {
        bloque_encabezado();
        bloque_variables();
        bloque_cuerpo();
    }

    // Bloque de cabezera del programa
    private void bloque_encabezado() throws Exception {
        match("PROGRAMA");
        match("IDENTIFICADOR_INICIO");
        match("PUNTOCOMA");
    }

    // Este es la parte donde se declaran las variables
    private void bloque_variables() throws Exception {
        match("VARIABLES");
        lista_variables();
    }

    private void lista_variables() throws Exception {
        if (tokenList.get(currentIndex).token.equals("IDENTIFICADOR")) {
            do {
                variable();
            } while (tokenList.get(currentIndex).token.equals("COMA"));
            match("DOSPUNTOS");
            tipoDato();
            match("PUNTOCOMA");
            lista_variables();
        }
    }

    private void variable() throws Exception {
        match("IDENTIFICADOR");
        if (tokenList.get(currentIndex).token.equals("COMA")) {
            match("COMA");
            variable();
        }
    }

    private void tipoDato() throws Exception {
        if (tokenList.get(currentIndex).token.equals("LOGICO")) {
            match("LOGICO");
        }
        if (tokenList.get(currentIndex).token.equals("ENTERO")) {
            match("ENTERO");
        }
        if (tokenList.get(currentIndex).token.equals("REAL")) {
            match("REAL");
        }
        if (tokenList.get(currentIndex).token.equals("CADENA")) {
            match("CADENA");
        }
    }

    // Cuerpo del programa aqui se supone que se estructura el codigo

    private void bloque_cuerpo() throws Exception {
        match("INICIO");
        if (!tokenList.get(currentIndex).token.equals("FIN")) {
            proceso();
        }
        match("FIN");
    }

    private void proceso() throws Exception {
        if (tokenList.get(currentIndex).token.equals("SI")) {
            estructura_condicional();
        }
        if (tokenList.get(currentIndex).token.equals("REPETIR")) {
            estructura_repetitiva();
        }
        if (tokenList.get(currentIndex).token.equals("MIENTRAS")) {
            estructura_mientras();
        }
        if (tokenList.get(currentIndex).token.equals("ESCRIBIR") || tokenList.get(currentIndex).token.equals("LEER")) {
            IO();
        }
    }

    private void estructura_condicional() throws Exception {
        match("SI");
        match("PARENTESISABIERTO");
        expresion();
        match("PARENTESISCERRADO");
        match("ENTONCES");
        bloque_cuerpo();
        match("SINO");
        bloque_cuerpo();
    }

    private void estructura_repetitiva() throws Exception {
        match("MIENTRAS");
        match("PARENTESISABIERTO");
        expresion();
        match("PARENTESISCERRADO");
        bloque_cuerpo();
        match("FINMIENTRAS");
    }

    private void estructura_mientras() throws Exception {
        match("MIENTRAS");
        match("PARENTESISABIERTO");
        expresion();
        match("PARENTESISCERRADO");
        match("HACER");
        bloque_cuerpo();
    }

    private void expresion() throws Exception {
        valor();
    }

    private void operador_relacional() throws Exception {

    }

    private void IO() throws Exception {
        if (tokenList.get(currentIndex).token.equals("ESCRIBIR")) {
            escritura();
        }
        if (tokenList.get(currentIndex).token.equals("LEER")) {
            lectura();
        }
    }

    private void escritura() throws Exception {
        match("ESCRIBIR");
        match("PARENTESISABIERTO");
        valor();
        match("PARENTESISCERRADO");
        match("PUNTOCOMA");
    }

    private void lectura() throws Exception {
        match("LEER");
        match("PARENTESISABIERTO");
        valor();
        match("PARENTESISCERRADO");
        match("PUNTOCOMA");
    }

    private void valor() throws Exception {
        if (tokenList.get(currentIndex).token.equals("IDENTIFICADOR")) {
            match("IDENTIFICADOR");
        }
        if (tokenList.get(currentIndex).token.equals("NUMERO_ENTERO")) {
            match("NUMERO_ENTERO");
        }
        if (tokenList.get(currentIndex).token.equals("NUMERO_REAL")) {
            match("NUMERO_REAL");
        }
        if (tokenList.get(currentIndex).token.equals("CONSTANTE_STRING")) {
            match("CONSTANTE_STRING");
        }
        if (tokenList.get(currentIndex).nombre.equals("VALOR_LOGICO")) {
            if (tokenList.get(currentIndex).token.equals("VERDADERO")) {
                match("VERDADERO");
            }
            if (tokenList.get(currentIndex).token.equals("FALSO")) {
                match("FALSO");
            }
        }
    }

    private void match(String expectedToken) throws Exception {
        Token currentToken = tokenList.get(currentIndex);
        System.out.println(tokenList.get(currentIndex).token);
        if (tokenList.get(currentIndex).token.equals(expectedToken)) {
            currentIndex++;
        } else {
            throw new Exception("Se esperaba '" + expectedToken + "', pero se encontró '" + currentToken.token + "'"
                    + " en la línea " + currentToken.numeroLinea);
        }
    }
}
