package aczg.grebechi;

import aczg.grebechi.view.cli.ContextoCLI;
import aczg.grebechi.view.cli.MotorTerminal;
import aczg.grebechi.view.cli.comandos.ComandoLimpar;
import aczg.grebechi.view.cli.comandos.ComandoSair;

public class Main {
    public static void main(String[] args) {
        MotorTerminal terminal = configurarTerminal();
        terminal.iniciar();
    }


    private static MotorTerminal configurarTerminal() {
        MotorTerminal motor = new MotorTerminal();
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoLimpar());
        motor.registrarComando(ContextoCLI.GLOBAL, new ComandoSair());

        return motor;
    }
}