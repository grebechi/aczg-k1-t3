package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.core.Status;
import aczg.grebechi.core.Tarefa;
import aczg.grebechi.service.QuadroService;
import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.util.DataUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.MotorTerminal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ComandoTarefa implements Comando {

    private final QuadroService quadroService;
    private final MotorTerminal motor;
    private final Scanner scanner;

    public ComandoTarefa(QuadroService quadroService, MotorTerminal motor) {
        this.quadroService = quadroService;
        this.motor = motor;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public List<String> getNomes() {
        return List.of("tarefa", "t");
    }

    @Override
    public String getDescricao() {
        return "Gerencia tarefas (criar, listar, ler, editar, remover). Use 't -t' para o tutorial.";
    }

    @Override
    public String getTutorial() {
        return ConsoleUtil.NEGRITO + ConsoleUtil.CIANO + "\nTUTORIAL: Comando Tarefa\n" + ConsoleUtil.RESET +
                ConsoleUtil.NEGRITO + "Ações CRUD:\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "criar" + ConsoleUtil.RESET + "            : Inicia o assistente de criação.\n" +
                "  " + ConsoleUtil.VERDE + "<id> ou ler <id>" + ConsoleUtil.RESET + " : Exibe os detalhes da tarefa formatados (Ex: t 1).\n" +
                "  " + ConsoleUtil.VERDE + "editar <id>" + ConsoleUtil.RESET + "      : Abre um menu interativo para alterar os dados da tarefa.\n" +
                "  " + ConsoleUtil.VERDE + "remover <id>" + ConsoleUtil.RESET + "     : Exclui a tarefa do quadro.\n\n" +
                ConsoleUtil.NEGRITO + "Listagem (Flags de ordenação/filtro):\n" + ConsoleUtil.RESET +
                "  " + ConsoleUtil.VERDE + "listar" + ConsoleUtil.RESET + "           : Lista ordenando por Prioridade (padrão).\n" +
                "  " + ConsoleUtil.VERDE + "listar -c" + ConsoleUtil.RESET + "        : Lista agrupando/ordenando por Categoria.\n" +
                "  " + ConsoleUtil.VERDE + "listar -d <data>" + ConsoleUtil.RESET + " : Lista tarefas a partir de uma data (Ex: t listar -d 20/10/2026).\n" +
                "  " + ConsoleUtil.VERDE + "listar -p" + ConsoleUtil.RESET + "        : Lista ordenando por Prioridade.\n" +
                "  " + ConsoleUtil.VERDE + "listar -s" + ConsoleUtil.RESET + "        : Lista agrupando/ordenando por Status.\n";
    }

    @Override
    public void executar(String[] argumentos) {
        if (argumentos.length == 0) {
            System.out.println(ConsoleUtil.AMARELO + "Faltam argumentos. Use 'tarefa -t' para ver o tutorial." + ConsoleUtil.RESET);
            return;
        }

        String acao = argumentos[0].toLowerCase();
        if (acao.equals("-t")) {
            System.out.println(getTutorial());
            return;
        }

        String quadroAtual = getQuadroAtualDoMotor();
        Quadro quadro = quadroService.getQuadro(quadroAtual);

        if (quadro == null) {
            System.out.println(ConsoleUtil.AMARELO + "Erro: Não foi possível acessar o quadro atual." + ConsoleUtil.RESET);
            return;
        }

        // Atalho: Se o primeiro argumento for um número, assume-se que é o comando "ler"
        if (acao.matches("\\d+")) {
            lerTarefa(quadro, acao);
            return;
        }

        switch (acao) {
            case "criar":
                criarTarefaGuiada(quadroAtual);
                break;
            case "listar":
                listarTarefas(quadro, argumentos);
                break;
            case "ler":
                if (argumentos.length < 2) {
                    System.out.println(ConsoleUtil.AMARELO + "Informe o ID da tarefa. Ex: t ler 1 ou t 1" + ConsoleUtil.RESET);
                } else {
                    lerTarefa(quadro, argumentos[1]);
                }
                break;
            case "editar":
                editarTarefa(quadro, argumentos);
                break;
            case "remover":
                removerTarefa(quadro, argumentos);
                break;
            default:
                System.out.println(ConsoleUtil.AMARELO + "Ação '" + acao + "' não reconhecida. Use 'tarefa -t'." + ConsoleUtil.RESET);
        }
    }

    // ========================================================================
    // MÉTODOS DE AÇÃO (CRUD)
    // ========================================================================

    private void criarTarefaGuiada(String quadroAtual) {
        System.out.println(ConsoleUtil.CIANO + "\n--- Nova Tarefa (Aperte ENTER para aceitar os padrões) ---" + ConsoleUtil.RESET);

        System.out.print("Nome (obrigatório): ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println(ConsoleUtil.AMARELO + "Nome não informado. Criação de tarefa cancelada.\n" + ConsoleUtil.RESET);
            return;
        }

        System.out.print("Descrição [Sem descrição]: ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isEmpty()) descricao = "Sem descrição";

        LocalDate dataTermino = null;
        while (dataTermino == null) {
            System.out.print("Data de Entrega (DD/MM/AAAA) [Hoje: " + DataUtil.formatarCurto(LocalDate.now()) + "]: ");
            String dataStr = scanner.nextLine().trim();
            if (dataStr.isEmpty()) {
                dataTermino = LocalDate.now();
            } else {
                while(!DataUtil.isDataValida(dataStr)){
                    System.out.print(ConsoleUtil.AMARELO + "Data inválida, informe DD/MM/AAAA ou AAAA-MM-DD: " + ConsoleUtil.RESET);
                    dataStr = scanner.nextLine().trim();
                }
                dataTermino = DataUtil.parse(dataStr);
            }
        }

        int prioridade = -1;
        while (prioridade < 1 || prioridade > 5) {
            System.out.print("Prioridade (1-5) [Padrão: 5]: ");
            String prioStr = scanner.nextLine().trim();
            if (prioStr.isEmpty()) {
                prioridade = 5;
            } else {
                try {
                    prioridade = Integer.parseInt(prioStr);
                    if (prioridade < 1 || prioridade > 5) {
                        System.out.println(ConsoleUtil.AMARELO + "A prioridade deve ser entre 1 e 5." + ConsoleUtil.RESET);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(ConsoleUtil.AMARELO + "Digite um número de 1 a 5 ou dê ENTER." + ConsoleUtil.RESET);
                }
            }
        }

        System.out.print("Categoria [Geral]: ");
        String categoria = scanner.nextLine().trim();
        if (categoria.isEmpty()) categoria = "Geral";

        try {
            Tarefa criada = quadroService.adicionarTarefa(quadroAtual, nome, descricao, dataTermino, prioridade, categoria);
            System.out.println(ConsoleUtil.VERDE + "\nTarefa '" + criada.getNome() + "' criada com sucesso! (ID: " + criada.getId() + ")\n" + ConsoleUtil.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleUtil.AMARELO + "Erro ao criar tarefa: " + e.getMessage() + ConsoleUtil.RESET);
        }
    }

    private void lerTarefa(Quadro quadro, String idStr) {
        Tarefa tarefa = buscarTarefaPorId(quadro, idStr);
        if (tarefa == null) return;

        System.out.println(ConsoleUtil.NEGRITO + "\n=== Detalhes da Tarefa #" + tarefa.getId() + " ===" + ConsoleUtil.RESET);
        System.out.println("Nome       : " + ConsoleUtil.CIANO + tarefa.getNome() + ConsoleUtil.RESET);
        System.out.println("Descrição  : " + tarefa.getDescricao());

        // Define a cor baseada no status
        String corStatus;
        if (tarefa.getStatus() == Status.DONE) corStatus = ConsoleUtil.VERDE;
        else if (tarefa.getStatus() == Status.DOING) corStatus = ConsoleUtil.CIANO;
        else corStatus = ConsoleUtil.AMARELO;

        System.out.println("Status     : " + corStatus + tarefa.getStatus() + ConsoleUtil.RESET);

        // Destaca se for prioridade máxima
        String corPrio = corDaPrioridade(tarefa);
        System.out.println("Prioridade : " + corPrio + tarefa.getPrioridade() + ConsoleUtil.RESET);

        System.out.println("Entrega    : " + DataUtil.formatarExtenso(tarefa.getDataTermino()));
        System.out.println("Categoria  : " + tarefa.getCategoria());
        System.out.println();
    }

    private void listarTarefas(Quadro quadro, String[] argumentos) {
        if (quadro.getTarefas().isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada neste quadro.");
            return;
        }

        List<Tarefa> tarefas = new ArrayList<>(quadro.getTarefas());
        String flag = argumentos.length > 1 ? argumentos[1].toLowerCase() : "-p";

        switch (flag) {
            case "-c":
                tarefas.sort(Comparator.comparing(Tarefa::getCategoria).thenComparing(Tarefa::getPrioridade));
                System.out.println(ConsoleUtil.NEGRITO + "\n=== Tarefas ordenadas por CATEGORIA ===" + ConsoleUtil.RESET);
                break;

            case "-d":
                LocalDate dataFiltro;
                String dataStr;

                // Se o usuário passou a data direto no comando (Ex: t listar -d 20/10/2026)
                if (argumentos.length > 2) {
                    dataStr = argumentos[2];
                } else {
                    // Se ele esqueceu, pede a data avisando que o ENTER pega o dia de hoje
                    System.out.print("Informe a data de início (DD/MM/AAAA) [Hoje: " + DataUtil.formatarCurto(LocalDate.now()) + "]: ");
                    dataStr = scanner.nextLine().trim();
                }

                // Se o usuário apenas apertou ENTER (string vazia)
                if (dataStr.isEmpty()) {
                    dataFiltro = LocalDate.now();
                } else {
                    while (!DataUtil.isDataValida(dataStr)) {
                        System.out.print(ConsoleUtil.AMARELO + "Data inválida! Informe DD/MM/AAAA (ou aperte ENTER para Hoje): " + ConsoleUtil.RESET);
                        dataStr = scanner.nextLine().trim();

                        if (dataStr.isEmpty()) {
                            break;
                        }
                    }
                    // Se ele saiu do while dando ENTER, pega hoje, senão faz o parse
                    dataFiltro = dataStr.isEmpty() ? LocalDate.now() : DataUtil.parse(dataStr);
                }

                // Filtramos removendo as tarefas cuja data é ANTERIOR à informada
                tarefas.removeIf(t -> t.getDataTermino().isBefore(dataFiltro));
                tarefas.sort(Comparator.comparing(Tarefa::getDataTermino).thenComparing(Tarefa::getPrioridade));

                System.out.println(ConsoleUtil.NEGRITO + "\n=== Tarefas a partir de " + DataUtil.formatarCurto(dataFiltro) + " ===" + ConsoleUtil.RESET);

                if (tarefas.isEmpty()) {
                    System.out.println("Nenhuma tarefa encontrada a partir desta data.\n");
                    return; // Sai antes de entrar no loop de impressão lá embaixo
                }
                break;

            case "-s":
                tarefas.sort(Comparator.comparing(t -> t.getStatus().getDescricao()));
                System.out.println(ConsoleUtil.NEGRITO + "\n=== Tarefas ordenadas por STATUS ===" + ConsoleUtil.RESET);
                break;

            case "-p":
            default:
                tarefas.sort(Comparator.comparingInt(Tarefa::getPrioridade));
                System.out.println(ConsoleUtil.NEGRITO + "\n=== Tarefas ordenadas por PRIORIDADE ===" + ConsoleUtil.RESET);
                break;
        }

        for (Tarefa t : tarefas) {
            if (t.getPrioridade() == 1) {
                String corDestaque = corDaPrioridade(t);
                System.out.println(corDestaque + ConsoleUtil.NEGRITO + " " + t + ConsoleUtil.RESET);
            } else {
                System.out.println(" " + t);
            }
        }
        System.out.println();
    }

    private String corDaPrioridade(Tarefa t){
        if (t.getPrioridade() == 1) {
            String corDestaque;

            // Define a cor baseada no status para prioridade 1
            switch (t.getStatus()) {
                case TODO:
                    corDestaque = ConsoleUtil.VERMELHO;
                    break;
                case DOING:
                    corDestaque = ConsoleUtil.AMARELO;
                    break;
                case DONE:
                    corDestaque = ConsoleUtil.VERDE;
                    break;
                default:
                    corDestaque = ConsoleUtil.RESET;
            }

            return corDestaque;
        } else {
            return ConsoleUtil.RESET;
        }
    }

    private void editarTarefa(Quadro quadro, String[] argumentos) {
        if (argumentos.length < 2) {
            System.out.println(ConsoleUtil.AMARELO + "Informe o ID da tarefa. Ex: t editar 1" + ConsoleUtil.RESET);
            return;
        }

        Tarefa tarefa = buscarTarefaPorId(quadro, argumentos[1]);
        if (tarefa == null) return;

        boolean editando = true;
        while (editando) {
            System.out.println(ConsoleUtil.CIANO + "\n--- Editando Tarefa #" + tarefa.getId() + " ---" + ConsoleUtil.RESET);
            System.out.println("1. Nome        : " + tarefa.getNome());
            System.out.println("2. Descrição   : " + tarefa.getDescricao());
            System.out.println("3. Data Término: " + DataUtil.formatarCurto(tarefa.getDataTermino()));
            System.out.println("4. Prioridade  : " + tarefa.getPrioridade());
            System.out.println("5. Categoria   : " + tarefa.getCategoria());
            System.out.println("6. Status      : " + tarefa.getStatus());
            System.out.println("0. Sair e Salvar");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.print("Novo nome: ");
                    String nNome = scanner.nextLine().trim();
                    if (!nNome.isEmpty()) tarefa.setNome(nNome);
                    break;
                case "2":
                    System.out.print("Nova descrição: ");
                    tarefa.setDescricao(scanner.nextLine().trim());
                    break;
                case "3":
                    System.out.print("Nova Data (DD/MM/AAAA): ");
                    String dataNova = scanner.nextLine().trim();
                    if (DataUtil.isDataValida(dataNova)) {
                        tarefa.setDataTermino(DataUtil.parse(dataNova));
                    } else {
                        System.out.println(ConsoleUtil.AMARELO + "Data inválida! Alteração ignorada." + ConsoleUtil.RESET);
                    }
                    break;
                case "4":
                    System.out.print("Nova Prioridade (1-5): ");
                    try {
                        int prio = Integer.parseInt(scanner.nextLine().trim());
                        if (prio >= 1 && prio <= 5) tarefa.setPrioridade(prio);
                        else System.out.println(ConsoleUtil.AMARELO + "Prioridade deve ser entre 1 e 5." + ConsoleUtil.RESET);
                    } catch (NumberFormatException e) {
                        System.out.println(ConsoleUtil.AMARELO + "Entrada inválida!" + ConsoleUtil.RESET);
                    }
                    break;
                case "5":
                    System.out.print("Nova Categoria: ");
                    tarefa.setCategoria(scanner.nextLine().trim());
                    break;
                case "6":
                    System.out.print("Novo Status (TODO, DOING, DONE): ");
                    try {
                        tarefa.setStatus(Status.valueOf(scanner.nextLine().trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println(ConsoleUtil.AMARELO + "Status inválido!" + ConsoleUtil.RESET);
                    }
                    break;
                case "0":
                    editando = false;
                    quadroService.salvarQuadro(quadro);
                    System.out.println(ConsoleUtil.VERDE + "Alterações concluídas!" + ConsoleUtil.RESET);
                    break;
                default:
                    System.out.println(ConsoleUtil.AMARELO + "Opção inválida." + ConsoleUtil.RESET);
            }
        }
    }

    private void removerTarefa(Quadro quadro, String[] argumentos) {
        if (argumentos.length < 2) {
            System.out.println(ConsoleUtil.AMARELO + "Informe o ID da tarefa. Ex: t remover 1" + ConsoleUtil.RESET);
            return;
        }

        Tarefa tarefa = buscarTarefaPorId(quadro, argumentos[1]);
        if (tarefa == null) return;

        quadro.getTarefas().remove(tarefa);
        quadroService.salvarQuadro(quadro);
        System.out.println(ConsoleUtil.VERDE + "Tarefa #" + tarefa.getId() + " removida com sucesso!" + ConsoleUtil.RESET);
    }

    private Tarefa buscarTarefaPorId(Quadro quadro, String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            return quadro.getTarefas().stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("ID não encontrado no quadro."));
        } catch (NumberFormatException e) {
            System.out.println(ConsoleUtil.AMARELO + "O ID deve ser um número." + ConsoleUtil.RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleUtil.AMARELO + e.getMessage() + ConsoleUtil.RESET);
        }
        return null;
    }

    private String getQuadroAtualDoMotor() {
        return motor.getIndicadorLocal();
    }
}