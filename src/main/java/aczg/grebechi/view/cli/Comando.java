package aczg.grebechi.view.cli;

import java.util.List;

public interface Comando {
    List<String> getNomes();
    String getDescricao();
    String getTutorial();
    void executar(String[] argumentos);
}