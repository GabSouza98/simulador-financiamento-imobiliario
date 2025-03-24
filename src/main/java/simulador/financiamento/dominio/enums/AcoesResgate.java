package simulador.financiamento.dominio.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum AcoesResgate {

    AMORTIZAR("Amortizar", 0),
    REINVESTIR("Reinvestir", 1);

    private final String nome;
    private final int index;

    public static AcoesResgate getByName(String name) {
        return Arrays.stream(AcoesResgate.values())
                .filter(acoes -> acoes.getNome().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Name not found"));
    }

}
