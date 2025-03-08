package simulador.financiamento.utils;

public final class Constants {

    private Constants(){}

    public static final double IMPOSTO_DE_RENDA = 0.14;
    public static final double RENDIMENTO_ANUAL_FGTS = 3.0;
    public static final double ALIQUOTA_MENSAL_FGTS = 8.0;

    public static final String HEADER_ROW = "Num Prestacao,Amortizacao,Parcela,Valor Extra,Valor Pago Mensal,Saldo Devedor";
    public static final String[] COLUMN_NAMES = {"Num Prestacao","Amortizacao","Parcela","Valor Extra","Valor Pago Mensal","Saldo Devedor"};

}
