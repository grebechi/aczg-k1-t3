package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.MotorTerminal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComandoAjuda implements Comando {
    private final MotorTerminal motor;

    public ComandoAjuda(MotorTerminal motor) {
        this.motor = motor;
    }

    @Override
    public List<String> getNomes() {
        return Arrays.asList("ajuda", "?", "help");
    }

    @Override
    public String getDescricao() {
        return "Lista os comandos. Use -g (globais), -c (contexto) ou -t (tutorial).";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Ajuda\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Lista os comandos disponíveis para uso no sistema.\n" +
                "  O sistema divide os comandos em duas categorias:\n" +
                "  - Globais: Funcionam em qualquer tela do sistema.\n" +
                "  - Contextuais: Funcionam apenas no quadro/menu onde você está agora.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Parâmetros Disponíveis:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "-g" + ConsoleUtil.RESET + " : Lista apenas os comandos globais.\n" +
                "  " + ConsoleUtil.VERDE + "-c" + ConsoleUtil.RESET + " : Lista apenas os comandos do contexto atual.\n" +
                "  " + ConsoleUtil.VERDE + "-t" + ConsoleUtil.RESET + " : Exibe este manual detalhado.\n\n" +
                ConsoleUtil.NEGRITO + "Exemplos de Uso:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + "ajuda" + ConsoleUtil.RESET + "    -> Mostra todos os comandos disponíveis.\n" +
                "  " + ConsoleUtil.AMARELO + "? -g" + ConsoleUtil.RESET + "       -> Mostra apenas os globais.\n" +
                "  " + ConsoleUtil.AMARELO + "help -c" + ConsoleUtil.RESET + "    -> Mostra apenas os do contexto atual.\n";
    }

    @Override
    public void executar(String[] argumentos) {
        boolean mostrarGlobal = true;
        boolean mostrarContexto = true;

        if (argumentos.length > 0) {
            String parametro = argumentos[0].toLowerCase();


            if (parametro.equals("-t")) {
                System.out.println(getTutorial());
                return;
            } else if (parametro.equals("-g")) {
                mostrarContexto = false;
            } else if (parametro.equals("-c")) {
                mostrarGlobal = false;
            } else {
                System.out.println(ConsoleUtil.AMARELO + "Aviso: Parâmetro '" + parametro + "' ignorado. Use -g, -c ou -t." + ConsoleUtil.RESET);
            }
        }

        System.out.println(ConsoleUtil.NEGRITO + "\n=== Ajuda: Comandos Disponíveis ===" + ConsoleUtil.RESET);

        if (mostrarGlobal) {
            System.out.println(ConsoleUtil.CIANO + "\n[ Comandos Globais ]" + ConsoleUtil.RESET);
            imprimirComandos(motor.getComandosGlobais());
        }

        if (mostrarContexto) {
            Map<String, Comando> comandosDaTela = motor.getComandosDoContextoAtual();
            if (!comandosDaTela.isEmpty()) {
                System.out.println(ConsoleUtil.CIANO + "\n[ Comandos do Contexto Atual ]" + ConsoleUtil.RESET);
                imprimirComandos(comandosDaTela);
            } else if (!mostrarGlobal) {
                System.out.println("  Nenhum comando específico para este contexto.");
            }
        }

        System.out.println(ConsoleUtil.NEGRITO + "\n===================================\n" + ConsoleUtil.RESET);
    }

    private void imprimirComandos(Map<String, Comando> mapaComandos) {
        Set<Comando> comandosUnicos = new HashSet<>(mapaComandos.values());

        for (Comando cmd : comandosUnicos) {
            String nomesFormatados = String.join(", ", cmd.getNomes());

            System.out.printf(" %s%-20s%s | %s\n",
                    ConsoleUtil.AMARELO,
                    nomesFormatados,
                    ConsoleUtil.RESET,
                    cmd.getDescricao()
            );
        }
    }
}