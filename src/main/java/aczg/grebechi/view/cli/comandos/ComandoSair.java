package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;

import java.util.List;

public class ComandoSair implements Comando {

    @Override
    public List<String> getNomes() {
        return List.of("sair", "tchau");
    }

    @Override
    public String getDescricao() {
        return "Encerra a aplicação. Use -t para o tutorial.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Sair\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Encerra a aplicação de forma segura, retornando ao terminal do seu sistema operacional.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Parâmetros Disponíveis:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "-t" + ConsoleUtil.RESET + " : Exibe este manual detalhado.\n\n" +
                ConsoleUtil.NEGRITO + "Exemplos de Uso:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + "sair" + ConsoleUtil.RESET + "    -> Encerra o sistema imediatamente.\n" +
                "  " + ConsoleUtil.AMARELO + "tchau" + ConsoleUtil.RESET + "   -> Forma alternativa de encerrar o sistema.\n";
    }

    @Override
    public void executar(String[] argumentos) {
        if (argumentos.length > 0) {
            String parametro = argumentos[0].toLowerCase();

            if (parametro.equals("-t")) {
                System.out.println(getTutorial());
            } else {
                System.out.println(ConsoleUtil.AMARELO + "Aviso: O comando sair não aceita o parâmetro '" + parametro + "'. Use apenas 'sair' ou '-t'." + ConsoleUtil.RESET);
            }

            return;
        }

        System.out.println(ConsoleUtil.VERDE + "Encerrando a aplicação. Até logo!" + ConsoleUtil.RESET);
        System.exit(0);
    }
}