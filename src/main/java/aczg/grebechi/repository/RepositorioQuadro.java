package aczg.grebechi.repository;

import aczg.grebechi.core.Quadro;
import aczg.grebechi.util.ArquivoUtil;
import aczg.grebechi.util.MapeadorJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioQuadro {

    // Nome da pasta onde todos os quadros serão salvos
    private static final String PASTA_DADOS = "dados";

    public RepositorioQuadro() {
        ArquivoUtil.garantirDiretorio(PASTA_DADOS);
    }

    public void salvar(Quadro quadro) {
        if (quadro == null || quadro.getNome() == null || quadro.getNome().trim().isEmpty()) {
            return;
        }

        String json = MapeadorJson.converterParaJson(quadro);
        String caminho = PASTA_DADOS + "/" + quadro.getNome().toLowerCase() + ".json";

        ArquivoUtil.salvarTexto(caminho, json);
    }

    public void deletar(String nomeQuadro) {
        String caminho = PASTA_DADOS + "/" + nomeQuadro.toLowerCase() + ".json";
        ArquivoUtil.deletarArquivo(caminho);
    }

    public Map<String, Quadro> carregarTodos() {
        Map<String, Quadro> quadrosMap = new HashMap<>();
        List<String> arquivos = ArquivoUtil.listarNomesArquivos(PASTA_DADOS);

        for (String nomeArquivo : arquivos) {
            if (nomeArquivo.endsWith(".json")) {
                String caminho = PASTA_DADOS + "/" + nomeArquivo;
                String json = ArquivoUtil.lerTexto(caminho);

                Quadro quadro = MapeadorJson.converterParaQuadro(json);

                if (quadro != null) {
                    // Guarda no Map usando o nome do quadro em minúsculo como chave
                    quadrosMap.put(quadro.getNome().toLowerCase(), quadro);
                }
            }
        }
        return quadrosMap;
    }
}