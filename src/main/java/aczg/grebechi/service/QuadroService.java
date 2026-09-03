package aczg.grebechi.service;

import aczg.grebechi.core.Quadro;

import java.util.HashMap;
import java.util.Map;

public class QuadroService {

    // Simula nosso banco de dados em memória
    private final Map<String, Quadro> quadros = new HashMap<>();

    public void criarQuadro(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do quadro não pode ser vazio.");
        }

        String nomeFormatado = nome.trim().toLowerCase();

        if (quadros.containsKey(nomeFormatado)) {
            throw new IllegalArgumentException("Já existe um quadro com o nome '" + nomeFormatado + "'.");
        }

        quadros.put(nomeFormatado, new Quadro(nomeFormatado));
    }

    public Quadro getQuadro(String nome) {
        return quadros.get(nome.trim().toLowerCase());
    }

    public Map<String, Quadro> getTodosQuadros() {
        return quadros;
    }
}