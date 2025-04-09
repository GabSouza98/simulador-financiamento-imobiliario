package simulador.financiamento.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
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

public class GraficoMultiplo extends JFrame {
    private JPanel graficoMultiploPanel;
    private JLabel selecioneGraficosLabel;
    private JPanel containerPanel;
    private JCheckBox amortizacaoBox;
    private JCheckBox parcelaBox;
    private JCheckBox valorExtraBox;
    private JCheckBox valorPagoBox;
    private JCheckBox saldoDevedorBox;
    private JCheckBox jurosBox;
    private JPanel itensPanel;
    private JButton graficoIndividualButton;
    private JButton graficoCombinadoButton;
    private JPanel buttonsPanel;

    private final ArrayList<SistemaAmortizacao> simulationsList;

    private final List<Integer> selectedIndexes = new ArrayList<>();

    public GraficoMultiplo(ArrayList<SistemaAmortizacao> simulationsList) {
        this.simulationsList = simulationsList;
        setContentPane(graficoMultiploPanel);
        setTitle("Gráfico Múltiplo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        amortizacaoBox.setSelected(true);
        jurosBox.setSelected(true);
        parcelaBox.setSelected(true);
        valorExtraBox.setSelected(false);
        valorPagoBox.setSelected(false);
        saldoDevedorBox.setSelected(true);

        selectedIndexes.add(1);
        selectedIndexes.add(2);
        selectedIndexes.add(3);
        selectedIndexes.add(6);

        amortizacaoBox.addActionListener(e -> {
            if (amortizacaoBox.isSelected()) {
                selectedIndexes.add(1);
            } else {
                selectedIndexes.remove((Object) 1);
            }
        });

        jurosBox.addActionListener(e -> {
            if (jurosBox.isSelected()) {
                selectedIndexes.add(2);
            } else {
                selectedIndexes.remove((Object) 2);
            }
        });

        parcelaBox.addActionListener(e -> {
            if (parcelaBox.isSelected()) {
                selectedIndexes.add(3);
            } else {
                selectedIndexes.remove((Object) 3);
            }
        });

        valorExtraBox.addActionListener(e -> {
            if (valorExtraBox.isSelected()) {
                selectedIndexes.add(4);
            } else {
                selectedIndexes.remove((Object) 4);
            }
        });

        valorPagoBox.addActionListener(e -> {
            if (valorPagoBox.isSelected()) {
                selectedIndexes.add(5);
            } else {
                selectedIndexes.remove((Object) 5);
            }
        });

        saldoDevedorBox.addActionListener(e -> {
            if (saldoDevedorBox.isSelected()) {
                selectedIndexes.add(6);
            } else {
                selectedIndexes.remove((Object) 6);
            }
        });

        graficoIndividualButton.addActionListener(e -> criarGraficos());
        graficoCombinadoButton.addActionListener(e -> criarGraficoCombinado());
    }

    private void criarGraficoCombinado() {
        NumberAxis xLabel = new NumberAxis("Número de Parcelas");
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(xLabel);

        //Ao total pode haver 7 coleções
        final XYSeriesCollection[] seriesCollection = new XYSeriesCollection[7];
        for (int i = 0; i < seriesCollection.length; i++) {
            seriesCollection[i] = new XYSeriesCollection();
        }

        simulationsList.forEach((sistemaAmortizacao) -> {

            //0 a 6. 0 é o número de parcelas, e de 1 a 6 são os dados relevantes
            final XYSeries[] series = new XYSeries[7];
            for (int i = 0; i < series.length; i++) {
                series[i] = new XYSeries(sistemaAmortizacao.getNomeFinanciamento());
            }

            List<String> tabela = sistemaAmortizacao.getTabela();
            //Starts at 2 to skip header in table and first row which is 0 in every column
            for (int i = 2; i < tabela.size(); i++) {
                String[] row = tabela.get(i).split(",");

                // Dessa maneira, iteramos uma vez só pela tabela, para cada simulação.
                // Numa rodada já coletamos todos os itens selecionados.
                selectedIndexes.forEach(index -> {
                    series[index].add(Double.parseDouble(row[0]), Double.parseDouble(row[index]));
                });
            }

            // Adiciona cada série à sua respectiva coleção
            for (int i = 0; i < seriesCollection.length; i++) {
                seriesCollection[i].addSeries(series[i]);
            }
        });

        selectedIndexes.forEach(index -> {
            //Cria um XYPlot para cada índice selecionado, cada um com seu próprio objeto renderer.
            final XYItemRenderer renderer = new StandardXYItemRenderer();
            renderer.setDefaultStroke(new BasicStroke(Constants.LINE_THICKNESS));
            ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);

            final NumberAxis rangeY = new NumberAxis(GraficoLabels.getGraficoLabelByIndex(index).getYLabel());
            final XYPlot subplot = new XYPlot(seriesCollection[index], null, rangeY, renderer);

            plot.add(subplot, 1);
        });

        plot.setGap(10.0);
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("Gráfico Combinado", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        //Opcao 1
        new StandardChartTheme("JFree").apply(chart);

        if (!plot.getSubplots().isEmpty()) {
            var firstRenderer = plot.getSubplots().get(0).getRenderer();

            for (int j = 1; j < plot.getSubplots().size(); j++) {
                var renderer = plot.getSubplots().get(j).getRenderer();
                for (int i = 0; i < seriesCollection[1].getSeriesCount(); i++) { //seriesCollection[1].getSeriesCount() também é igual a simulationsList.size()
                    //www.jfree.org/forum/viewtopic.php?t=30258
                    //Busca a cor usada em cada série no primeiro gráfico e aplica na série correspondente
                    renderer.setSeriesPaint(i, firstRenderer.getItemPaint(i, 0));
                    //Faz com que a legenda não fique visível (não se repita), pois ela tem sempre o mesmo nome para cada série (sistemaAmortizacao.getNomeFinanciamento())
                    renderer.setSeriesVisibleInLegend(i, false);
                }
            }
        }

        ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        panel.setMouseZoomable(true, false);

        JFrame chartFrame = new JFrame();
        chartFrame.setMinimumSize(new Dimension(1200, 1000));
        chartFrame.setTitle("Gráfico Combinado");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setContentPane(panel);
        chartFrame.pack();
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
    }

    private void criarGraficos() {
        selectedIndexes.forEach(this::criarGrafico);
    }

    private void criarGrafico(int index) {
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
        ChartPanel chartPanel = new ChartPanel(jFreeChart, true, true, true, true, true);
        showChart(graficoLabel.getTitulo(), chartPanel);
    }

    private void showChart(String name, ChartPanel chartPanel) {
        JFrame dialogFrame = new JFrame();
        dialogFrame.setMinimumSize(new Dimension(600, 400));
        dialogFrame.setTitle(name);
        dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialogFrame.setContentPane(chartPanel);
        dialogFrame.pack();
        dialogFrame.setLocationRelativeTo(null);
        dialogFrame.setVisible(true);
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
        graficoMultiploPanel = new JPanel();
        graficoMultiploPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        graficoMultiploPanel.setMinimumSize(new Dimension(400, 200));
        graficoMultiploPanel.setPreferredSize(new Dimension(400, 200));
        selecioneGraficosLabel = new JLabel();
        Font selecioneGraficosLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, selecioneGraficosLabel.getFont());
        if (selecioneGraficosLabelFont != null) selecioneGraficosLabel.setFont(selecioneGraficosLabelFont);
        selecioneGraficosLabel.setHorizontalAlignment(0);
        selecioneGraficosLabel.setHorizontalTextPosition(0);
        selecioneGraficosLabel.setText("Selecione os itens desejados");
        graficoMultiploPanel.add(selecioneGraficosLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), null, 0, false));
        containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        graficoMultiploPanel.add(containerPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, -1), new Dimension(400, -1), new Dimension(400, 300), 0, false));
        itensPanel = new JPanel();
        itensPanel.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        containerPanel.add(itensPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(150, -1), new Dimension(150, 189), new Dimension(150, -1), 0, false));
        amortizacaoBox = new JCheckBox();
        amortizacaoBox.setText("Amortização");
        itensPanel.add(amortizacaoBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        itensPanel.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        parcelaBox = new JCheckBox();
        parcelaBox.setText("Parcela");
        itensPanel.add(parcelaBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorExtraBox = new JCheckBox();
        valorExtraBox.setText("Valor Extra");
        itensPanel.add(valorExtraBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorPagoBox = new JCheckBox();
        valorPagoBox.setText("Valor Pago");
        itensPanel.add(valorPagoBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saldoDevedorBox = new JCheckBox();
        saldoDevedorBox.setText("Saldo Devedor");
        itensPanel.add(saldoDevedorBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        itensPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        jurosBox = new JCheckBox();
        jurosBox.setText("Júros");
        itensPanel.add(jurosBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        containerPanel.add(buttonsPanel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(200, -1), new Dimension(200, -1), new Dimension(200, -1), 0, false));
        graficoIndividualButton = new JButton();
        graficoIndividualButton.setText("Gerar gráficos individuais");
        graficoIndividualButton.setToolTipText("Gera um gráfico para cada item selecionado.");
        buttonsPanel.add(graficoIndividualButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), new Dimension(200, -1), 0, false));
        graficoCombinadoButton = new JButton();
        graficoCombinadoButton.setText("Gerar gráfico combinado");
        graficoCombinadoButton.setToolTipText("Gera um gráfico de cada item selecionado, mantendo o eixo X alinhado, possibilitando observar a evolução de cada item no decorrer do financiamento.");
        buttonsPanel.add(graficoCombinadoButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), new Dimension(200, -1), 0, false));
        final Spacer spacer3 = new Spacer();
        buttonsPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        buttonsPanel.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        containerPanel.add(spacer5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        containerPanel.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        containerPanel.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        return graficoMultiploPanel;
    }

}
