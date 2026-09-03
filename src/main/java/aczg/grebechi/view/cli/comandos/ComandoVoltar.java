package aczg.grebechi.view.cli.comandos;

import aczg.grebechi.util.ConsoleUtil;
import aczg.grebechi.view.cli.Comando;
import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;

import java.util.List;

public class ComandoVoltar implements Comando {
    private final MotorTerminal motor;

    public ComandoVoltar(MotorTerminal motor) {
        this.motor = motor;
    }

    @Override
    public List<String> getNomes() {
        return List.of("voltar", "v");
    }

    @Override
    public String getDescricao() {
        return "Sai do quadro atual e retorna ao menu principal.";
    }

    @Override
    public String getTutorial() {
        return "Uso: voltar\nRetorna o contexto da aplicação para o menu principal.";
    }

    @Override
    public void executar(String[] argumentos) {
        motor.setContextoAtual(ContextoCLI.MENU_PRINCIPAL);
        motor.setIndicadorLocal("sem-quadro");
        System.out.println(ConsoleUtil.VERDE + "Voltando ao menu principal..." + ConsoleUtil.RESET);
    }
}