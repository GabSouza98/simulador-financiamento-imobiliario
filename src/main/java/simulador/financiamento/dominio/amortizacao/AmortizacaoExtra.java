package simulador.financiamento.dominio.amortizacao;

import java.util.List;

public interface AmortizacaoExtra {

    Boolean deveAmortizar(Integer numeroParcelas);

    Double amortizar();

    void calcularMes();

    static <T extends AmortizacaoExtra> T buscarAmortizacaoExtra(Class<T> clazz, List<AmortizacaoExtra> amortizacaoExtraList) {
        return amortizacaoExtraList.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Classe não encontrada"));
    }

}
