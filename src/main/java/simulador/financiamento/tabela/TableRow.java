package simulador.financiamento.tabela;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class TableRow implements Serializable {
    private final Integer numeroParcelas;
    private final Double valorParcela;
    private final Double valorExtra;
    private final Double valorPagoMensal;
    private final Double saldoDevedor;
}
