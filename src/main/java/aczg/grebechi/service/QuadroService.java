package aczg.grebechi.service;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.core.Status;
import aczg.grebechi.core.Tarefa;
import aczg.grebechi.repository.RepositorioQuadro;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class QuadroService {

    private final Map<String, Quadro> quadros = new HashMap<>();
    private final RepositorioQuadro repositorio;

    public QuadroService() {
        this.repositorio = new RepositorioQuadro();

        Map<String, Quadro> quadrosCarregados = this.repositorio.carregarTodos();
        if (quadrosCarregados != null && !quadrosCarregados.isEmpty()) {
            this.quadros.putAll(quadrosCarregados);
        }
    }

    public void criarQuadro(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do quadro não pode ser vazio.");
        }

        String nomeFormatado = nome.trim().toLowerCase();

        if (quadros.containsKey(nomeFormatado)) {
            throw new IllegalArgumentException("Já existe um quadro com o nome '" + nomeFormatado + "'.");
        }

        Quadro novoQuadro = new Quadro(nomeFormatado);
        quadros.put(nomeFormatado, novoQuadro);

        repositorio.salvar(novoQuadro);
    }

    public Quadro getQuadro(String nome) {
        return quadros.get(nome.trim().toLowerCase());
    }

    public Map<String, Quadro> getTodosQuadros() {
        return quadros;
    }

    public void removerQuadro(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do quadro não pode ser vazio.");
        }

        String nomeFormatado = nome.trim().toLowerCase();

        if (!quadros.containsKey(nomeFormatado)) {
            throw new IllegalArgumentException("Quadro '" + nomeFormatado + "' não encontrado.");
        }

        quadros.remove(nomeFormatado);

        repositorio.deletar(nomeFormatado);
    }

    public void renomearQuadro(String nomeAntigo, String nomeNovo) {
        if (nomeAntigo == null || nomeAntigo.trim().isEmpty() || nomeNovo == null || nomeNovo.trim().isEmpty()) {
            throw new IllegalArgumentException("Os nomes dos quadros não podem ser vazios.");
        }

        String antigoFormatado = nomeAntigo.trim().toLowerCase();
        String novoFormatado = nomeNovo.trim().toLowerCase();

        if (!quadros.containsKey(antigoFormatado)) {
            throw new IllegalArgumentException("Quadro '" + antigoFormatado + "' não encontrado.");
        }

        if (quadros.containsKey(novoFormatado)) {
            throw new IllegalArgumentException("Já existe um quadro com o nome '" + novoFormatado + "'.");
        }

        Quadro quadro = quadros.remove(antigoFormatado);
        quadro.setNome(novoFormatado);
        quadros.put(novoFormatado, quadro);

        // DELETA O ARQUIVO VELHO E SALVA O NOVO
        repositorio.deletar(antigoFormatado);
        repositorio.salvar(quadro);
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

        repositorio.salvar(quadro);

        return novaTarefa;
    }

    public void salvarQuadro(Quadro quadro) {
        if (quadro != null) {
            repositorio.salvar(quadro);
        }
    }

    private void rebalancearPrioridades(Quadro quadro) {
        quadro.getTarefas().sort(Comparator.comparingInt(Tarefa::getPrioridade));
    }
}