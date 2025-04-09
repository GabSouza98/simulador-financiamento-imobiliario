package simulador.financiamento.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulador.financiamento.dominio.enums.GraficoLabels;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacao;
import simulador.financiamento.utils.Constants;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GraficoIndividual extends JFrame {
    private JPanel buttonsGroup;
    private JRadioButton amortizacaoRadio;
    private JRadioButton parcelaRadio;
    private JRadioButton valorExtraRadio;
    private JRadioButton valorPagoRadio;
    private JRadioButton saldoDevedorRadio;
    private JRadioButton jurosRadio;
    private JPanel graficoPanel;
    private JLabel selecioneGraficoLabel;
    private JPanel graficoIndividualPanel;

    private ButtonGroup buttonGroup;

    private final ArrayList<SistemaAmortizacao> simulationsList;

    public GraficoIndividual(ArrayList<SistemaAmortizacao> simulationsList) {
        this.simulationsList = simulationsList;
        setContentPane(graficoIndividualPanel);
        setTitle("Gráfico Individual");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(amortizacaoRadio);
        buttonGroup.add(parcelaRadio);
        buttonGroup.add(valorExtraRadio);
        buttonGroup.add(valorPagoRadio);
        buttonGroup.add(saldoDevedorRadio);
        buttonGroup.add(jurosRadio);

        amortizacaoRadio.setSelected(true);

        amortizacaoRadio.addActionListener(e -> criarGrafico(1));
        jurosRadio.addActionListener(e -> criarGrafico(2));
        parcelaRadio.addActionListener(e -> criarGrafico(3));
        valorExtraRadio.addActionListener(e -> criarGrafico(4));
        valorPagoRadio.addActionListener(e -> criarGrafico(5));
        saldoDevedorRadio.addActionListener(e -> criarGrafico(6));

        criarGrafico(1);
    }

    public void criarGrafico(int index) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();

        simulationsList.forEach((sistemaAmortizacao) -> {
            XYSeries series = new XYSeries(sistemaAmortizacao.getNomeFinanciamento());

            List<String> tabela = sistemaAmortizacao.getTabela();
            //Starts at 2 to skip header in table and first row which is 0 in every column
            for (int i = 2; i < tabela.size(); i++) {
                String[] row = tabela.get(i).split(",");

                series.add(Double.parseDouble(row[0]), Double.parseDouble(row[index]));
            }

            seriesCollection.addSeries(series);

        });

        GraficoLabels graficoLabel = GraficoLabels.getGraficoLabelByIndex(index);
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(graficoLabel.getTitulo(), "Número de Parcelas", graficoLabel.getYLabel(), seriesCollection);
        XYItemRenderer renderer = jFreeChart.getXYPlot().getRenderer();
        renderer.setDefaultStroke(new BasicStroke(Constants.LINE_THICKNESS));
        ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);

        ChartPanel chartPanel = new ChartPanel(jFreeChart,
                900, 600,
                900, 600,
                900, 600,
                true, true, true, true, true, true, true);

        graficoPanel.removeAll();
        graficoPanel.add(chartPanel);
        graficoPanel.revalidate();
        graficoPanel.repaint();
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        graficoIndividualPanel = new JPanel();
        graficoIndividualPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        graficoIndividualPanel.setMinimumSize(new Dimension(900, 660));
        graficoIndividualPanel.setPreferredSize(new Dimension(900, 660));
        selecioneGraficoLabel = new JLabel();
        Font selecioneGraficoLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, selecioneGraficoLabel.getFont());
        if (selecioneGraficoLabelFont != null) selecioneGraficoLabel.setFont(selecioneGraficoLabelFont);
        selecioneGraficoLabel.setHorizontalAlignment(0);
        selecioneGraficoLabel.setHorizontalTextPosition(0);
        selecioneGraficoLabel.setText("Selecione o tipo de gráfico");
        graficoIndividualPanel.add(selecioneGraficoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        buttonsGroup = new JPanel();
        buttonsGroup.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        graficoIndividualPanel.add(buttonsGroup, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(564, 30), null, 0, false));
        amortizacaoRadio = new JRadioButton();
        amortizacaoRadio.setText("Amortização");
        buttonsGroup.add(amortizacaoRadio, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parcelaRadio = new JRadioButton();
        parcelaRadio.setText("Parcela");
        buttonsGroup.add(parcelaRadio, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorExtraRadio = new JRadioButton();
        valorExtraRadio.setText("Valor Extra");
        buttonsGroup.add(valorExtraRadio, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorPagoRadio = new JRadioButton();
        valorPagoRadio.setText("Valor Pago");
        buttonsGroup.add(valorPagoRadio, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saldoDevedorRadio = new JRadioButton();
        saldoDevedorRadio.setText("Saldo Devedor");
        buttonsGroup.add(saldoDevedorRadio, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        buttonsGroup.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        buttonsGroup.add(spacer2, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        jurosRadio = new JRadioButton();
        jurosRadio.setText("Júros");
        buttonsGroup.add(jurosRadio, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        graficoPanel = new JPanel();
        graficoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        graficoIndividualPanel.add(graficoPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(900, 600), new Dimension(900, 600), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return graficoIndividualPanel;
    }

}
