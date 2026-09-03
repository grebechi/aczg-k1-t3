package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.service.QuadroService;
import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;

import java.util.List;
import java.util.Map;

public class ComandoQuadro implements Comando {
    private final QuadroService quadroService;
    private final MotorTerminal motor;

    public ComandoQuadro(QuadroService quadroService, MotorTerminal motor) {
        this.quadroService = quadroService;
        this.motor = motor;
    }

    @Override
    public List<String> getNomes() {
        return List.of("quadro", "q");
    }

    @Override
    public String getDescricao() {
        return "Gerencia os quadros (novo, listar, entrar). Use -t para o tutorial.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Quadro\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Permite criar, listar e navegar entre os seus quadros de tarefas.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Ações:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "novo <nome>" + ConsoleUtil.RESET + "   : Cria um novo quadro em branco.\n" +
                "  " + ConsoleUtil.VERDE + "listar" + ConsoleUtil.RESET + "        : Mostra todos os quadros criados.\n" +
                "  " + ConsoleUtil.VERDE + "entrar <nome>" + ConsoleUtil.RESET + " : Entra no quadro especificado (muda o prompt).\n";
    }

    @Override
    public void executar(String[] argumentos) {
        if (argumentos.length == 0) {
            System.out.println(ConsoleUtil.AMARELO + "Faltam argumentos. Use 'quadro -t' para o tutorial." + ConsoleUtil.RESET);
            return;
        }

        String acao = argumentos[0].toLowerCase();

        if (acao.equals("-t")) {
            System.out.println(getTutorial());
            return;
        }

        switch (acao) {
            case "novo":
                if (argumentos.length < 2) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o nome. Ex: quadro novo faculdade" + ConsoleUtil.RESET);
                    return;
                }
                try {
                    String nome = argumentos[1];
                    quadroService.criarQuadro(nome);
                    System.out.println(ConsoleUtil.VERDE + "Quadro '" + nome + "' criado com sucesso!" + ConsoleUtil.RESET);
                } catch (IllegalArgumentException e) {
                    System.out.println(ConsoleUtil.AMARELO + "Erro: " + e.getMessage() + ConsoleUtil.RESET);
                }
                break;

            case "listar":
                Map<String, Quadro> quadros = quadroService.getTodosQuadros();
                if (quadros.isEmpty()) {
                    System.out.println("Nenhum quadro criado ainda.");
                } else {
                    System.out.println(ConsoleUtil.NEGRITO + "\n=== Quadros Disponíveis ===" + ConsoleUtil.RESET);
                    for (String nome : quadros.keySet()) {
                        System.out.println(" - " + nome);
                    }
                    System.out.println();
                }
                break;

            case "entrar":
                if (argumentos.length < 2) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o nome. Ex: quadro entrar faculdade" + ConsoleUtil.RESET);
                    return;
                }
                String nomeQuadro = argumentos[1].toLowerCase();
                if (quadroService.getQuadro(nomeQuadro) != null) {
                    motor.setIndicadorLocal(nomeQuadro);
                    motor.setContextoAtual(ContextoCLI.DENTRO_DO_QUADRO);
                    System.out.println(ConsoleUtil.VERDE + "Entrando no quadro: " + nomeQuadro + ConsoleUtil.RESET);
                } else {
                    System.out.println(ConsoleUtil.AMARELO + "Quadro '" + nomeQuadro + "' não encontrado." + ConsoleUtil.RESET);
                }
                break;

            default:
                System.out.println(ConsoleUtil.AMARELO + "Ação '" + acao + "' não reconhecida. Use 'quadro -t'." + ConsoleUtil.RESET);
        }
    }
}