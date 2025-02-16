package simulador.financiamento.utils;

public class Conversor {

    private Conversor(){}

    public static double converterTaxaAnualParaMensal(Double taxaAnual) {
        return Math.pow(1 + taxaAnual/100, (double) 1/12) - 1;
    }

    public static double converterTaxaMensalParaAnual(Double taxaMensal) {
        return Math.pow(1+taxaMensal, 12) - 1;
    }

}
