package simulador.financiamento.utils;

public class RendaCerta {

    private RendaCerta(){}

    public static double calcularPrestacao(double valorAtual, double i, int n) {
        return valorAtual / calcularTermoExponencial(i, n);
    }

    public static double calcularValorAtual(double prestacao, double i, int n) {
        return prestacao * calcularTermoExponencial(i, n);
    }

    public static double calcularTermoExponencial(double i, int n) {
        return (Math.pow(1+i, n) - 1) / (i * Math.pow(1+i, n));
    }

}
