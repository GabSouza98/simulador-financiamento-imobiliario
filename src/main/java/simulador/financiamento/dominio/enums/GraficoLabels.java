package simulador.financiamento.dominio.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum GraficoLabels {

    AMORTIZACAO(1, "Amortização x Número de Parcelas", "Amortização"),
    JUROS(2, "Valor Júros x Número de Parcelas", "Valor Júros"),
    PARCELA(3, "Valor Parcela x Número de Parcelas", "Valor Parcela"),
    VALOR_EXTRA(4, "Valor Extra x Número de Parcelas", "Valor Extra"),
    VALOR_PAGO(5, "Valor Pago x Número de Parcelas", "Valor Pago"),
    SALDO_DEVEDOR(6, "Saldo Devedor x Número de Parcelas", "Saldo Devedor");

    private final int index;
    private final String titulo;
    private final String yLabel;

    public static GraficoLabels getGraficoLabelByIndex(int index) {
        return Arrays.stream(GraficoLabels.values())
                .filter(e -> e.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid Index"));
    }
}
