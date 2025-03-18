package simulador.financiamento.utils;

public final class Constants {

    private Constants(){}

    public static final double IMPOSTO_DE_RENDA = 0.14;
    public static final double RENDIMENTO_ANUAL_FGTS = 3.0;
    public static final double ALIQUOTA_MENSAL_FGTS = 8.0;

    public static final String HEADER_ROW = "Nº Parcela,Amortizacao,Juros,Parcela,Valor Extra,Valor Pago Mensal,Saldo Devedor";
    public static final String[] SIMULATION_COLUMN_NAMES = {"Nº Parcela","Amortizacao","Juros","Parcela","Valor Extra","Valor Pago Mensal","Saldo Devedor"};

    public static final String[] COMPARISON_COLUMN_NAMES = {"Nome","Nº Parcelas/Prazo","Taxa Júros","Valor Imóvel","Valor Entrada","Valor Pago","VP/VI","Valorização","Inflação"};



}
