package aczg.grebechi.util;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.core.Status;
import aczg.grebechi.core.Tarefa;

import java.time.LocalDate;
import java.util.List;

public class MapeadorJson {

    // CONVERTER DE JAVA PARA TEXTO (JSON)
    public static String converterParaJson(Quadro quadro) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"nome\": \"").append(quadro.getNome()).append("\",\n");
        sb.append("  \"tarefas\": [\n");

        List<Tarefa> tarefas = quadro.getTarefas();
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            sb.append("    {\n");
            sb.append("      \"id\": ").append(t.getId()).append(",\n");
            sb.append("      \"nome\": \"").append(t.getNome()).append("\",\n");
            sb.append("      \"descricao\": \"").append(t.getDescricao()).append("\",\n");
            sb.append("      \"prioridade\": ").append(t.getPrioridade()).append(",\n");
            sb.append("      \"categoria\": \"").append(t.getCategoria()).append("\",\n");
            sb.append("      \"status\": \"").append(t.getStatus().name()).append("\",\n");
            sb.append("      \"dataTermino\": \"").append(t.getDataTermino() != null ? t.getDataTermino().toString() : "").append("\"\n");
            sb.append("    }");

            // Adiciona vírgula se não for a última tarefa
            if (i < tarefas.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    // CONVERTER DE TEXTO (JSON) PARA JAVA
    public static Quadro converterParaQuadro(String json) {
        if (json == null || json.trim().isEmpty()) return null;

        // Extrai o nome do quadro
        String nomeQuadro = extrairString(json, "nome");
        Quadro quadro = new Quadro(nomeQuadro);

        // Separa o bloco de tarefas
        int inicioArray = json.indexOf("\"tarefas\": [");
        if (inicioArray == -1) return quadro;

        String blocoTarefas = json.substring(inicioArray);

        // Vamos quebrar o texto por blocos de objetos JSON "{ ... }"
        String[] blocos = blocoTarefas.split("\\{");

        // O índice 0 é lixo antes da primeira chave '{', começamos do 1
        for (int i = 1; i < blocos.length; i++) {
            String bloco = blocos[i];

            // Extração dos dados do bloco de texto
            int id = extrairInt(bloco, "id");
            String nome = extrairString(bloco, "nome");
            String descricao = extrairString(bloco, "descricao");
            int prioridade = extrairInt(bloco, "prioridade");
            String categoria = extrairString(bloco, "categoria");
            String statusStr = extrairString(bloco, "status");
            Status status = statusStr.isEmpty() ? Status.TODO : Status.valueOf(statusStr);

            String dataStr = extrairString(bloco, "dataTermino");
            LocalDate dataTermino = dataStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dataStr);

            Tarefa t = new Tarefa(id, nome, descricao, dataTermino, prioridade, categoria, status);
            quadro.adicionarTarefa(t);
        }

        return quadro;
    }

    private static String extrairString(String texto, String chave) {
        String busca = "\"" + chave + "\": \"";
        int inicio = texto.indexOf(busca);
        if (inicio == -1) return "";
        inicio += busca.length();
        int fim = texto.indexOf("\"", inicio);
        return fim != -1 ? texto.substring(inicio, fim) : "";
    }

    private static int extrairInt(String texto, String chave) {
        String busca = "\"" + chave + "\": ";
        int inicio = texto.indexOf(busca);
        if (inicio == -1) return 0;
        inicio += busca.length();
        int fim = texto.indexOf(",", inicio);
        if (fim == -1) fim = texto.indexOf("\n", inicio); // Caso seja o último item

        try {
            return Integer.parseInt(texto.substring(inicio, fim).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}