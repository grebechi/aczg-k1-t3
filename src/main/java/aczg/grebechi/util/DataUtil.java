package aczg.grebechi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class DataUtil {

    private static final Locale LOCALE_PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter FORMATO_CURTO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Construtor privado para impedir instanciação (classe estritamente utilitária)
    private DataUtil() {
    }

    public static String formatarCurto(LocalDate data) {
        if (data == null) return "Sem data";
        return data.format(FORMATO_CURTO);
    }

    public static String formatarExtenso(LocalDate data) {
        if (data == null) return "Sem data";

        String diaSemana = data.format(DateTimeFormatter.ofPattern("EEE", LOCALE_PT_BR));
        String dia = String.valueOf(data.getDayOfMonth());
        String mes = data.format(DateTimeFormatter.ofPattern("MMMM", LOCALE_PT_BR));
        String ano = String.valueOf(data.getYear());

        // Limpa possíveis pontos gerados nativamente pelo Java no "EEE" e capitaliza
        diaSemana = capitalizar(diaSemana.replace(".", ""));
        mes = capitalizar(mes);

        return String.format("%s. %s de %s de %s", diaSemana, dia, mes, ano);
    }

    /**
     * Converte uma String (texto) em LocalDate.
     * Suporta tanto o padrão brasileiro (DD/MM/AAAA) quanto o ISO (AAAA-MM-DD).
     */
    public static LocalDate parse(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }

        texto = texto.trim();

        try {
            if (texto.contains("/")) {
                return LocalDate.parse(texto, FORMATO_CURTO);
            } else if (texto.contains("-")) {
                return LocalDate.parse(texto, FORMATO_ISO);
            } else {
                throw new IllegalArgumentException("Use '/' para DD/MM/AAAA ou '-' para AAAA-MM-DD.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Verifique o dia e o mês digitados.");
        }
    }

    public static boolean isDataValida(String texto) {
        try {
            parse(texto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String capitalizar(String palavra) {
        if (palavra == null || palavra.isEmpty()) return palavra;
        return palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();
    }
}