package aczg.grebechi.core;

import java.time.LocalDate;

public class Tarefa {
    private Integer id;
    private String nome;
    private String descricao;
    private LocalDate dataTermino;
    private int prioridade;
    private String categoria;
    private Status status;

    public Tarefa(Integer id, String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria, Status status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.setPrioridade(prioridade);
        this.categoria = categoria;
        this.status = status;
    }

    // --- Validação da Regra de Negócio ---
    public void setPrioridade(int prioridade) {
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("A prioridade deve ser um valor entre 1 e 5.");
        }
        this.prioridade = prioridade;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataTermino() { return dataTermino; }
    public void setDataTermino(LocalDate dataTermino) { this.dataTermino = dataTermino; }

    public int getPrioridade() { return prioridade; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%d] %s (Prioridade: %d, Categoria: %s, Status: %s) - Vence em: %s",
                id, nome, prioridade, categoria, status.getDescricao(), dataTermino);
    }
}