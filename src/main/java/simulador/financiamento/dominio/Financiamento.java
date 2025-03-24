package simulador.financiamento.dominio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class Financiamento implements Serializable {
    private final String nomeFinanciamento;
    private final Double valorImovel;
    private final Double percentualEntrada;
    private final Double jurosAnual;
    private final Integer prazo;
}
