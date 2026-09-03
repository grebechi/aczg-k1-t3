package aczg.grebechi.util;

public class ConsoleUtil {

    // Constantes ANSI
    public static final String RESET = "\u001B[0m";
    public static final String NEGRITO = "\u001B[1m";
    public static final String VERDE = "\u001B[32m";
    public static final String CIANO = "\u001B[36m";
    public static final String AMARELO = "\u001B[33m";
    public static final String ROXO = "\u001B[35m";
    private static final String LIMPAR_TELA = "\033[H\033[2J";

    public static void limparTela() {
        try {
            String sistemaOperacional = System.getProperty("os.name");

            if (sistemaOperacional.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.print(LIMPAR_TELA);
            System.out.flush();
        }
    }

    /**
     * Formata o prompt no estilo Shell (ex: Zsh/Bash).
     * Deixa o indicador (quadro) em Ciano e o nome do app em Verde.
     */
    public static String formatarPrompt(String indicadorLocal) {
        return CIANO + NEGRITO + "[" + indicadorLocal + "] "
                + VERDE + "aczg@grebechi"
                + RESET + " >> ";
    }
}