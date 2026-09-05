package aczg.grebechi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArquivoUtil {

    // Cria a pasta caso ela não exista
    public static void garantirDiretorio(String caminhoPasta) {
        Path caminho = Paths.get(caminhoPasta);
        if (!Files.exists(caminho)) {
            try {
                Files.createDirectories(caminho);
            } catch (IOException e) {
                System.out.println(ConsoleUtil.VERMELHO + "Erro ao criar diretório base: " + e.getMessage() + ConsoleUtil.RESET);
            }
        }
    }

    // Escreve um texto inteiro dentro de um arquivo
    public static void salvarTexto(String caminhoArquivo, String conteudo) {
        try {
            Files.writeString(Paths.get(caminhoArquivo), conteudo);
        } catch (IOException e) {
            System.out.println(ConsoleUtil.VERMELHO + "Erro ao salvar o arquivo: " + e.getMessage() + ConsoleUtil.RESET);
        }
    }

    // Lê um arquivo inteiro e retorna como String
    public static String lerTexto(String caminhoArquivo) {
        try {
            return Files.readString(Paths.get(caminhoArquivo));
        } catch (IOException e) {
            System.out.println(ConsoleUtil.VERMELHO + "Erro ao ler o arquivo: " + e.getMessage() + ConsoleUtil.RESET);
            return "";
        }
    }

    // Retorna a lista de nomes dos arquivos (ex: "estudos.json", "trabalho.json") dentro da pasta
    public static List<String> listarNomesArquivos(String caminhoPasta) {
        garantirDiretorio(caminhoPasta);
        try (Stream<Path> caminhos = Files.list(Paths.get(caminhoPasta))) {
            return caminhos
                    .filter(Files::isRegularFile)
                    .map(caminho -> caminho.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(ConsoleUtil.VERMELHO + "Erro ao listar arquivos: " + e.getMessage() + ConsoleUtil.RESET);
            return new ArrayList<>();
        }
    }

    // Exclui o arquivo físico (útil para remover um Quadro)
    public static void deletarArquivo(String caminhoArquivo) {
        try {
            Files.deleteIfExists(Paths.get(caminhoArquivo));
        } catch (IOException e) {
            System.out.println(ConsoleUtil.VERMELHO + "Erro ao deletar o arquivo: " + e.getMessage() + ConsoleUtil.RESET);
        }
    }
}