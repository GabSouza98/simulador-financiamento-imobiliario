package simulador.financiamento;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import simulador.financiamento.forms.SimuladorFinanciamento;

import javax.swing.*;

public class MainSwing {

    public static void main(String[] args) {

        FlatArcDarkOrangeIJTheme.setup();

        SwingUtilities.invokeLater(() -> {
            SimuladorFinanciamento simuladorFinanciamento = new SimuladorFinanciamento();
            simuladorFinanciamento.setVisible(true);
        });
    }
}
