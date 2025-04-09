package simulador.financiamento.utils;

import java.awt.*;
import static org.jfree.chart.ChartColor.*;

public final class Constants {

    private Constants(){}

    public static final double IMPOSTO_DE_RENDA = 0.15;
    public static final double RENDIMENTO_ANUAL_FGTS = 3.0;
    public static final double ALIQUOTA_MENSAL_FGTS = 8.0;

    public static final String HEADER_ROW = "Nº Parcela,Amortizacao,Juros,Parcela,Valor Extra,Valor Pago Mensal,Saldo Devedor";
    public static final String[] SIMULATION_COLUMN_NAMES = {"Nº Parcela","Amortizacao","Juros","Parcela","Valor Extra","Valor Pago Mensal","Saldo Devedor"};

    public static final String[] COMPARISON_COLUMN_NAMES = {"Nome","Nº Parcelas/Prazo","Taxa Júros","Valor Imóvel","Valor Entrada","Valor Pago","VP/VI","Valorização","Inflação"};

    public static final float LINE_THICKNESS = 2.5f;
    public static final Paint[] PAINT_ARRAY = new Color[] {
            new Color(255, 85, 85),
            new Color(85, 85, 255),
            new Color(85, 255, 85),
//            new Color(255, 255, 85),
            new Color(255, 85, 255),
            new Color(85, 255, 255),
            Color.PINK,
            Color.GRAY,
            DARK_RED,
            DARK_BLUE,
            DARK_GREEN,
//            DARK_YELLOW,
            DARK_MAGENTA,
            DARK_CYAN,
            Color.DARK_GRAY,
            LIGHT_RED,
            LIGHT_BLUE,
            LIGHT_GREEN,
//            LIGHT_YELLOW,
            LIGHT_MAGENTA,
            LIGHT_CYAN,
            Color.LIGHT_GRAY,
            VERY_DARK_RED,
            VERY_DARK_BLUE,
            VERY_DARK_GREEN,
//            VERY_DARK_YELLOW,
            VERY_DARK_MAGENTA,
            VERY_DARK_CYAN,
            VERY_LIGHT_RED,
            VERY_LIGHT_BLUE,
            VERY_LIGHT_GREEN,
//            VERY_LIGHT_YELLOW,
            VERY_LIGHT_MAGENTA,
            VERY_LIGHT_CYAN
    };

}
