package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.core.Status; // Importado para contar o status das tarefas
import aczg.grebechi.service.QuadroService;
import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ComandoQuadro implements Comando {
    private final QuadroService quadroService;
    private final MotorTerminal motor;
    private final Scanner scanner;

    public ComandoQuadro(QuadroService quadroService, MotorTerminal motor) {
        this.quadroService = quadroService;
        this.motor = motor;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public List<String> getNomes() {
        return List.of("quadro", "q");
    }

    @Override
    public String getDescricao() {
        return "Gerencia os quadros (novo, listar, entrar, apagar). Use -t para o tutorial.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Quadro\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Descrição:\n" + ConsoleUtil.RESET +
                "  Permite criar, listar, navegar, renomear e excluir os seus quadros de tarefas.\n\n" +
                ConsoleUtil.NEGRITO + "Apelidos:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.AMARELO + getNomes() + ConsoleUtil.RESET + "\n\n" +
                ConsoleUtil.NEGRITO + "Ações:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "novo <nome>" + ConsoleUtil.RESET + "              : Cria um novo quadro em branco.\n" +
                "  " + ConsoleUtil.VERDE + "listar" + ConsoleUtil.RESET + "                   : Mostra os quadros e o resumo de suas tarefas.\n" +
                "  " + ConsoleUtil.VERDE + "entrar <nome>" + ConsoleUtil.RESET + "            : Entra no quadro especificado.\n" +
                "  " + ConsoleUtil.VERDE + "renomear <antigo> <novo>" + ConsoleUtil.RESET + " : Renomeia um quadro existente.\n" +
                "  " + ConsoleUtil.VERDE + "apagar <nome>" + ConsoleUtil.RESET + "            : Exclui um quadro (pede confirmação se houver tarefas).\n";
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
                    for (Map.Entry<String, Quadro> entry : quadros.entrySet()) {
                        String nomeQuadro = entry.getKey();
                        Quadro q = entry.getValue();

                        int total = q.getTarefas().size();
                        long todo = q.getTarefas().stream().filter(t -> t.getStatus() == Status.TODO).count();
                        long doing = q.getTarefas().stream().filter(t -> t.getStatus() == Status.DOING).count();
                        long done = q.getTarefas().stream().filter(t -> t.getStatus() == Status.DONE).count();

                        System.out.printf(" - %s (Total: %d | " + ConsoleUtil.AMARELO + "TODO: %d" + ConsoleUtil.RESET + " | " + ConsoleUtil.CIANO + "DOING: %d" + ConsoleUtil.RESET + " | " + ConsoleUtil.VERDE + "DONE: %d" + ConsoleUtil.RESET + ")\n",
                                nomeQuadro, total, todo, doing, done);
                    }
                    System.out.println();
                }
                break;

            case "entrar":
                if (argumentos.length < 2) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o nome. Ex: quadro entrar faculdade" + ConsoleUtil.RESET);
                    return;
                }
                String nomeEntrar = argumentos[1].toLowerCase();
                if (quadroService.getQuadro(nomeEntrar) != null) {
                    motor.setIndicadorLocal(nomeEntrar);
                    motor.setContextoAtual(ContextoCLI.DENTRO_DO_QUADRO);
                    System.out.println(ConsoleUtil.VERDE + "Entrando no quadro: " + nomeEntrar + ConsoleUtil.RESET);
                } else {
                    System.out.println(ConsoleUtil.AMARELO + "Quadro '" + nomeEntrar + "' não encontrado." + ConsoleUtil.RESET);
                }
                break;

            case "renomear":
                if (argumentos.length < 3) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o nome antigo e o novo. Ex: quadro renomear faculdade trabalho" + ConsoleUtil.RESET);
                    return;
                }
                String nomeAntigo = argumentos[1].toLowerCase();
                String nomeNovo = argumentos[2].toLowerCase();
                try {
                    quadroService.renomearQuadro(nomeAntigo, nomeNovo);
                    System.out.println(ConsoleUtil.VERDE + "Quadro renomeado de '" + nomeAntigo + "' para '" + nomeNovo + "' com sucesso!" + ConsoleUtil.RESET);

                    // Se o usuário acabou de renomear o quadro onde ele está agora,
                    // atualizamos o indicador do motor para não quebrar o prompt!
                    if (nomeAntigo.equals(motor.getIndicadorLocal())) {
                        motor.setIndicadorLocal(nomeNovo);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(ConsoleUtil.AMARELO + "Erro: " + e.getMessage() + ConsoleUtil.RESET);
                }
                break;

            case "apagar":
                if (argumentos.length < 2) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o nome. Ex: quadro apagar faculdade" + ConsoleUtil.RESET);
                    return;
                }
                String nomeApagar = argumentos[1].toLowerCase();
                Quadro quadroApagar = quadroService.getQuadro(nomeApagar);

                if (quadroApagar == null) {
                    System.out.println(ConsoleUtil.AMARELO + "Quadro '" + nomeApagar + "' não encontrado." + ConsoleUtil.RESET);
                    return;
                }

                // Verifica se o quadro tem tarefas para exigir confirmação de segurança
                if (!quadroApagar.getTarefas().isEmpty()) {
                    System.out.println(ConsoleUtil.AMARELO + "\nAVISO: O quadro '" + nomeApagar + "' possui " + quadroApagar.getTarefas().size() + " tarefa(s)!" + ConsoleUtil.RESET);
                    System.out.println(ConsoleUtil.AMARELO + "Todas as tarefas serão perdidas." + ConsoleUtil.RESET);
                    System.out.print("Para confirmar a exclusão, digite " + ConsoleUtil.NEGRITO + "'" + nomeApagar + "'" + ConsoleUtil.RESET + " ou dê ENTER para cancelar: ");

                    String confirmacao = scanner.nextLine().trim();

                    if (!confirmacao.equals(nomeApagar)) {
                        System.out.println(ConsoleUtil.VERDE + "Exclusão cancelada. O quadro foi mantido." + ConsoleUtil.RESET);
                        return;
                    }
                }

                // Efetua a remoção (Certifique-se de que removerQuadro existe no seu QuadroService)
                quadroService.removerQuadro(nomeApagar);
                System.out.println(ConsoleUtil.VERDE + "Quadro '" + nomeApagar + "' apagado com sucesso!" + ConsoleUtil.RESET);

                // Se o usuário apagou o quadro em que ele estava dentro no momento, ele deve ser "expulso" para o menu principal
                if (nomeApagar.equals(motor.getIndicadorLocal())) {
                    motor.setIndicadorLocal("");
                    // Substitua MENU_PRINCIPAL pelo Enum correspondente ao seu contexto raiz se for diferente
                    motor.setContextoAtual(ContextoCLI.MENU_PRINCIPAL);
                }
                break;

            default:
                System.out.println(ConsoleUtil.AMARELO + "Ação '" + acao + "' não reconhecida. Use 'quadro -t'." + ConsoleUtil.RESET);
        }
    }
}