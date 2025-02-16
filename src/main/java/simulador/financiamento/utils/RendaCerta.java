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

    public static void main(String[] args) {
//        double taxaMensal = Conversor.converterTaxaAnualParaMensal(10.83);
//        System.out.println(taxaMensal);
//        System.out.println(calcularValorAtual(1250.91, taxaMensal, 359));
        System.out.println(calcularPrestacao(80000, 0.02, 5));
    }

}
