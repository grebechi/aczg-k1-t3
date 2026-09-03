package aczg.grebechi.view.cli;

import aczg.grebechi.util.ConsoleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MotorTerminal {
    private final Map<ContextoCLI, Map<String, Comando>> comandosPorContexto = new HashMap<>();

    private ContextoCLI contextoAtual = ContextoCLI.MENU_PRINCIPAL;

    private String indicadorLocal = "sem-quadro";

    private final Scanner scanner = new Scanner(System.in);

    private GerenciadorHistorico gerenciadorHistorico;

    public MotorTerminal() {
        for (ContextoCLI contexto : ContextoCLI.values()) {
            comandosPorContexto.put(contexto, new HashMap<>());
        }
    }

    public void registrarComando(ContextoCLI contexto, Comando comando) {
        for (String alias : comando.getNomes()) {
            comandosPorContexto.get(contexto).put(alias.toLowerCase(), comando);
        }
    }

    public Map<String, Comando> getComandosDoContextoAtual() {
        return comandosPorContexto.get(contextoAtual);
    }

    public Map<String, Comando> getComandosGlobais() {
        return comandosPorContexto.get(ContextoCLI.GLOBAL);
    }

    public void setGerenciadorHistorico(GerenciadorHistorico gerenciador) {
        this.gerenciadorHistorico = gerenciador;
    }

    public void setIndicadorLocal(String indicadorLocal) {
        this.indicadorLocal = indicadorLocal;
    }

    public void setContextoAtual(ContextoCLI contextoAtual) {
        this.contextoAtual = contextoAtual;
    }

    public void iniciar() {
        System.out.println("=== ACZG-Hero JAVA CLI ===");

        while (true) {
            System.out.print(ConsoleUtil.formatarPrompt(indicadorLocal));
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) continue;

            String entradaProcessada = entrada;

            // 1. Resolve o atalho (!1, !!), se houver
            if (gerenciadorHistorico != null && entradaProcessada.startsWith("!")) {
                entradaProcessada = gerenciadorHistorico.expandirAtalho(entradaProcessada);
                if (entradaProcessada == null) continue; // Se o atalho for inválido, interrompe

                System.out.println(ConsoleUtil.AMARELO + entradaProcessada + ConsoleUtil.RESET);
            }

            // 2. Extrai as partes para descobrir qual é o comando
            String[] partes = entradaProcessada.split("\\s+", 2);
            String nomeComando = partes[0].toLowerCase();
            String[] argumentos = partes.length > 1 ? partes[1].split("\\s+") : new String[0];

            Comando comandoEncontrado = getComandosDoContextoAtual().get(nomeComando);
            if (comandoEncontrado == null) {
                comandoEncontrado = getComandosGlobais().get(nomeComando);
            }

            // 3. Validação e Execução
            if (comandoEncontrado != null) {
                // SÓ SALVA NO HISTÓRICO SE O COMANDO FOR VÁLIDO!
                if (gerenciadorHistorico != null) {
                    gerenciadorHistorico.registrar(entradaProcessada);
                }

                comandoEncontrado.executar(argumentos);
            } else {
                System.out.println(ConsoleUtil.AMARELO + "Comando '" + nomeComando + "' não reconhecido. Digite 'ajuda' ou '?'." + ConsoleUtil.RESET);
            }
        }
    }
}