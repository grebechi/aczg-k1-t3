package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.GerenciadorHistorico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComandoHistorico implements Comando, GerenciadorHistorico {

    private final List<String> historico = new ArrayList<>();

    @Override
    public void registrar(String entrada) {
        historico.add(entrada);
    }

    @Override
    public String expandirAtalho(String entrada) {
        if (historico.isEmpty()) {
            System.out.println(ConsoleUtil.AMARELO + "O histórico está vazio." + ConsoleUtil.RESET);
            return null;
        }

        if (entrada.equals("!!")) {
            return historico.get(historico.size() - 1); // Continua sendo o último
        }

        try {
            // Se ele digitou !1, offset é 1. Se digitou !2, offset é 2.
            int offset = Integer.parseInt(entrada.substring(1));

            // Inverte o índice: O tamanho da lista menos o offset
            int index = historico.size() - offset;

            if (offset > 0 && index >= 0 && index < historico.size()) {
                return historico.get(index);
            } else {
                System.out.println(ConsoleUtil.AMARELO + "Comando não encontrado. O histórico possui " + historico.size() + " comandos válidos." + ConsoleUtil.RESET);
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleUtil.AMARELO + "Sintaxe inválida. Use '!!' ou '!<número>'." + ConsoleUtil.RESET);
            return null;
        }
    }

    @Override
    public List<String> getNomes() {
        return Arrays.asList("historico", "h");
    }

    @Override
    public String getDescricao() {
        return "Mostra os comandos digitados. Ex: 'h', 'h <número>' ou 'h -t'.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Histórico\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Exibe a lista de comandos previamente executados com sucesso.\n" +
                "  A lista funciona como uma pilha: o número '1' é sempre o comando mais recente.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Parâmetros Disponíveis:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "<número>" + ConsoleUtil.RESET + " : Limita a exibição aos últimos N comandos.\n" +
                "  " + ConsoleUtil.VERDE + "-t" + ConsoleUtil.RESET + "       : Exibe este manual detalhado.\n\n" +
                ConsoleUtil.NEGRITO + "Atalhos de Execução (Estilo Unix):\n" + ConsoleUtil.RESET +
                "  Você pode executar comandos diretamente do histórico digitando:\n" +
                "  " + ConsoleUtil.AMARELO + "!!" + ConsoleUtil.RESET + "         -> Executa o último comando imediatamente.\n" +
                "  " + ConsoleUtil.AMARELO + "!<número>" + ConsoleUtil.RESET + "  -> Executa o comando correspondente ao número na lista.\n\n" +
                ConsoleUtil.NEGRITO + "Exemplos de Uso:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + "h" + ConsoleUtil.RESET + "          -> Lista todo o histórico.\n" +
                "  " + ConsoleUtil.AMARELO + "h 5" + ConsoleUtil.RESET + "        -> Lista apenas os 5 comandos mais recentes.\n" +
                "  " + ConsoleUtil.AMARELO + "!1" + ConsoleUtil.RESET + "         -> Roda o comando 1 da lista (o mais recente).\n" +
                "  " + ConsoleUtil.AMARELO + "!3" + ConsoleUtil.RESET + "         -> Roda o 3º comando mais recente.\n";
    }

    @Override
    public void executar(String[] argumentos) {
        if (historico.isEmpty()) {
            System.out.println("Nenhum comando no histórico ainda.");
            return;
        }

        int limite = historico.size();

        // Trata os argumentos se houver
        if (argumentos.length > 0) {
            String parametro = argumentos[0].toLowerCase();

            // Intercepta a flag de tutorial ANTES de tentar converter para número
            if (parametro.equals("-t")) {
                System.out.println(getTutorial());
                return;
            }

            try {
                int qtdSolicitada = Integer.parseInt(parametro);
                if (qtdSolicitada > 0 && qtdSolicitada < limite) {
                    limite = qtdSolicitada;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleUtil.AMARELO + "Parâmetro inválido. Use um número ou '-t'. Mostrando todo o histórico." + ConsoleUtil.RESET);
            }
        }

        System.out.println(ConsoleUtil.NEGRITO + "\n=== Histórico de Comandos ===" + ConsoleUtil.RESET);

        // Imprime do mais antigo (dentro do limite) para o mais recente (que ficará na base)
        for (int i = limite - 1; i >= 0; i--) {
            int realIndex = historico.size() - 1 - i;
            int numeroVisual = i + 1; // 1 é o mais recente, ficando por último na impressão

            System.out.printf(" %3d | %s\n", numeroVisual, historico.get(realIndex));
        }

        System.out.println(ConsoleUtil.CIANO + "\nDica: Digite '!1' para executar o último comando ou '!!'." + ConsoleUtil.RESET);
        System.out.println(ConsoleUtil.NEGRITO + "=============================\n" + ConsoleUtil.RESET);
    }
}