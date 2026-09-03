package aczg.grebechi.core;

import java.util.ArrayList;
import java.util.List;

public class Quadro {
    private String nome;
    private List<Tarefa> tarefas;

    public Quadro(String nome) {
        this.nome = nome;
        this.tarefas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }

    public boolean removerTarefaPorId(Integer id) {
        return this.tarefas.removeIf(tarefa -> tarefa.getId().equals(id));
    }
}
