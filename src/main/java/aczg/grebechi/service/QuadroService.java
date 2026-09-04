package aczg.grebechi.service;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.core.Status;
import aczg.grebechi.core.Tarefa;

import java.time.LocalDate;
import java.util.Comparator;
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

    public Tarefa adicionarTarefa(String nomeQuadro, String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria) {
        Quadro quadro = getQuadro(nomeQuadro);
        if (quadro == null) {
            throw new IllegalArgumentException("Quadro '" + nomeQuadro + "' não encontrado.");
        }

        int novoId = quadro.getTarefas().size() + 1;

        Tarefa novaTarefa = new Tarefa(novoId, nome, descricao, dataTermino, prioridade, categoria, Status.TODO);

        quadro.adicionarTarefa(novaTarefa);

        rebalancearPrioridades(quadro);

        return novaTarefa;
    }

    private void rebalancearPrioridades(Quadro quadro) {
        quadro.getTarefas().sort(Comparator.comparingInt(Tarefa::getPrioridade));
    }
}