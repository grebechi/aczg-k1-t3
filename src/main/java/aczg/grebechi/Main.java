package aczg.grebechi;

import aczg.grebechi.service.QuadroService;
import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;
import aczg.grebechi.view.cli.comandos.*;

public class Main {
    public static void main(String[] args) {
        MotorTerminal terminal = configurarTerminal();
        terminal.iniciar();
    }


    private static MotorTerminal configurarTerminal() {
        MotorTerminal motor = new MotorTerminal();
        ComandoHistorico cmdHistorico = new ComandoHistorico();
        QuadroService quadroService = new QuadroService();
        ComandoQuadro cmdQuadro = new ComandoQuadro(quadroService, motor);
        motor.registrarComando(ContextoCLI.DENTRO_DO_QUADRO, new ComandoVoltar(motor));
        motor.registrarComando(ContextoCLI.GLOBAL, cmdQuadro);
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoLimpar());
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoSair());
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoAjuda(motor));
        motor.registrarComando(ContextoCLI.GLOBAL, cmdHistorico);
        motor.setGerenciadorHistorico(cmdHistorico);

        return motor;
    }
}