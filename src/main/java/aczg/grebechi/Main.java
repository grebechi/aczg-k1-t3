package aczg.grebechi;

import aczg.grebechi.view.cli.MotorTerminal;

public class Main {
    public static void main(String[] args) {
        MotorTerminal terminal = configurarTerminal();
        terminal.iniciar();
    }


    private static MotorTerminal configurarTerminal() {
        MotorTerminal motor = new MotorTerminal();

        return motor;
    }
}