package aczg.grebechi.core;

public enum Status {
    TODO("A Fazer"),
    DOING("Em Andamento"),
    DONE("Concluído");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}