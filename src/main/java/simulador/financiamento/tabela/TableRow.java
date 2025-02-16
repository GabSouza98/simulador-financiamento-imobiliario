package simulador.financiamento.tabela;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableRow {
    private final Integer numeroParcelas;
    private final Double valorParcela;
    private final Double valorExtra;
    private final Double valorPagoMensal;
    private final Double saldoDevedor;
}
