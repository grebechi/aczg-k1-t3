package aczg.grebechi;

import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;
import aczg.grebechi.view.cli.comandos.ComandoAjuda;
import aczg.grebechi.view.cli.comandos.ComandoHistorico;
import aczg.grebechi.view.cli.comandos.ComandoLimpar;
import aczg.grebechi.view.cli.comandos.ComandoSair;

public class Main {
    public static void main(String[] args) {
        MotorTerminal terminal = configurarTerminal();
        terminal.iniciar();
    }


    private static MotorTerminal configurarTerminal() {
        MotorTerminal motor = new MotorTerminal();
        ComandoHistorico cmdHistorico = new ComandoHistorico();
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoLimpar());
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoSair());
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoAjuda(motor));
        motor.registrarComando(ContextoCLI.GLOBAL, cmdHistorico);
        motor.setGerenciadorHistorico(cmdHistorico);

        return motor;
    }
}