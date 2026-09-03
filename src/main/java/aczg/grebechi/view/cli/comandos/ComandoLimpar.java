package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;

import java.util.Arrays;
import java.util.List;

public class ComandoLimpar implements Comando {

    @Override
    public List<String> getNomes() {
        return Arrays.asList("limpar", "clear", "cls");
    }

    @Override
    public String getDescricao() {
        return "Limpa a tela do terminal. Use -t para o tutorial.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Limpar\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Limpa todo o texto visível e o buffer do terminal, retornando o prompt\n" +
                "  para o topo da tela. Mantém o sistema limpo e organizado.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Parâmetros Disponíveis:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "-t" + ConsoleUtil.RESET + " : Exibe este manual detalhado.\n\n" +
                ConsoleUtil.NEGRITO + "Exemplos de Uso:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + "limpar" + ConsoleUtil.RESET + "   -> Limpa a tela imediatamente.\n" +
                "  " + ConsoleUtil.AMARELO + "clear" + ConsoleUtil.RESET + "    -> Alias padrão do Linux/Mac para limpar a tela.\n" +
                "  " + ConsoleUtil.AMARELO + "cls" + ConsoleUtil.RESET + "      -> Alias padrão do Windows para limpar a tela.\n";
    }

    @Override
    public void executar(String[] argumentos) {
        if (argumentos.length > 0) {
            String parametro = argumentos[0].toLowerCase();

            if (parametro.equals("-t")) {
                System.out.println(getTutorial());
            } else {
                System.out.println(ConsoleUtil.AMARELO + "Aviso: O comando limpar não aceita o parâmetro '" + parametro + "'. Use apenas 'limpar' ou '-t'." + ConsoleUtil.RESET);
            }

            return;
        }

        ConsoleUtil.limparTela();
    }
}