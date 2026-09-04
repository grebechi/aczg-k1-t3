package aczg.grebechi.core;

public enum Status {
    TODO("1 - A Fazer"),
    DOING("2 - Em Andamento"),
    DONE("3 - Concluído");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}