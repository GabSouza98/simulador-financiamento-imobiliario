package simulador.financiamento.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

public final class Constants {

    private Constants(){}

    public static final double IMPOSTO_DE_RENDA = 0.15;
    public static final double RENDIMENTO_ANUAL_FGTS = 3.0;
    public static final double ALIQUOTA_MENSAL_FGTS = 8.0;

    public static final String HEADER_ROW = "Nº Parcela;Amortizacao;Juros;Parcela;Valor Extra;Valor Pago Mensal;Saldo Devedor";

    public static final String[] SIMULATION_COLUMN_NAMES = {"Nº Parcela","Amortizacao","Juros","Parcela","Valor Extra","Valor Pago Mensal","Saldo Devedor"};

    public static final String[] COMPARISON_COLUMN_NAMES = {"Nome","Nº Parcelas/Prazo","Taxa Júros","Valor Imóvel","Valor Entrada","Valor Pago","VP/VI","Valorização","Inflação"};

    public static final float LINE_THICKNESS = 2.5f;

    public static final String DELIMITER = ";";

    public static final DecimalFormat decimalFormatter = new DecimalFormat("###,###,###,###,##0.##");

    public static Pattern pattern = Pattern.compile("^[+-]?([0-9]{1,3}(\\.[0-9]{3})*(,[0-9]+)?|\\d*,\\d+|\\d+)$");

    public static String sanitize(String input) {
        String inputWithoutDots = input.replace(".", "").replace(",", ".");
        String inputDoubleFormat = inputWithoutDots.replace(",", ".");

        String[] parts = inputDoubleFormat.split("\\.");

        boolean isInteger = true;

        if (parts.length == 2) {
            String decimalPart = parts[1];
            for (char c : decimalPart.toCharArray()) {
                if (c != '0') {
                    isInteger = false;
                    break;
                }
            }
        }

        return isInteger ? parts[0] : inputDoubleFormat;
    }
}
