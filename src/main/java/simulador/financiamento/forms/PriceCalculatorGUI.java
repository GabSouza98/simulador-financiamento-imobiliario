package simulador.financiamento.forms;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulador.financiamento.tabela.ExcelWriter;
import simulador.financiamento.dominio.FGTS;
import simulador.financiamento.dominio.RendimentoPassivo;
import simulador.financiamento.dominio.SistemaAmortizacaoEnum;
import simulador.financiamento.sistemas.amortizacao.PRICE;
import simulador.financiamento.sistemas.amortizacao.SAC;
import simulador.financiamento.sistemas.amortizacao.SistemaAmortizacao;
import simulador.financiamento.tabela.TableRow;
import simulador.financiamento.utils.Constants;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PriceCalculatorGUI extends JFrame {

    private JPanel contentPane;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField nomeFinanciamento;
    private JTextField valorImovel;
    private JSlider percentualEntrada;
    private JTextField jurosAnual;
    private JTextField valorExtraInicial;
    private JTextField percentProximoValorExtra;
    private JTextField valorExtraMinimo;
    private JTextField valorInvestido;
    private JTextField rendimentoAnual;
    private JPanel bottomLeftPanel;
    private JPanel upperLeftPanel;
    private JPanel nomeFinanciamentoPanel;
    private JPanel valorImovelPanel;
    private JPanel percentualEntradaPanel;
    private JPanel jurosAnualPanel;
    private JPanel valorExtraInicialPanel;
    private JPanel percentualProximoValorExtraPanel;
    private JPanel valorExtraMinimoPanel;
    private JPanel valorInvestidoPanel;
    private JPanel rendimentoAnualPanel;
    private JButton calcularButton;
    private JTable priceJTable;
    private JTabbedPane tabs;
    private JLabel valorPagoTotalField;
    private JLabel numeroParcelasField;
    private JLabel ratioValorPagoTotal;
    private JSplitPane verticalSplitPlane;
    private JSplitPane horizontalSplitPane;
    private JLabel nomeFinanciamentoLabel;
    private JLabel valorImovelLabel;
    private JLabel percentualEntradaLabel;
    private JLabel jurosAnualLabel;
    private JLabel valorExtraInicialLabel;
    private JLabel percentProximoValorExtraLabel;
    private JLabel valorExtraMinimoLabel;
    private JLabel valorInvestidoLabel;
    private JLabel rendimentoAnualLabel;
    private JButton changeThemeButton;
    private JButton compararSimulacoesButton;
    private JButton downloadExcelButton;
    private JPanel salarioBrutoPanel;
    private JTextField salarioBruto;
    private JPanel saldoAtualPanel;
    private JTextField saldoFGTS;
    private JPanel resetarCamposPanel;
    private JCheckBox resetarCamposBox;
    private JPanel escolherSistemaPanel;
    private JComboBox<String> sistemaAmortizacaoComboBox;
    private JPanel prazoPanel;
    private JLabel prazoLabel;
    private JTextField prazo;

    private final ExcelWriter excelWriter = new ExcelWriter();

    private boolean darkTheme = true;

    private final Map<Integer, SistemaAmortizacao> simulationsMap = new HashMap<>();

    public PriceCalculatorGUI() {

        setTitle("Simulador de Financiamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        getContentPane().setBackground(Color.black);
        pack();
        // Set the frame location to the center of the screen
        setLocationRelativeTo(null);

        defineOpcoesSistemasAmortizacao();
        defineFontes();

        calcularButton.addActionListener(e -> calcularFinanciamento());
        changeThemeButton.addActionListener(e -> toggleTheme());
        compararSimulacoesButton.addActionListener(e -> criarGrafico());
        downloadExcelButton.addActionListener(e -> {
            if (tabs.getTabCount() > 0) {
                int selectedTab = tabs.getSelectedIndex();
                SistemaAmortizacao sistemaAmortizacao = simulationsMap.get(selectedTab);
                excelWriter.writeTable(sistemaAmortizacao);
            }
        });

        tabs.addChangeListener(e -> {
            if (!simulationsMap.isEmpty() && simulationsMap.size() == tabs.getTabCount()) {
                int selectedTab = tabs.getSelectedIndex();
                SistemaAmortizacao sistemaAmortizacao = simulationsMap.get(selectedTab);
                updateFields(sistemaAmortizacao);
            }
        });

        // Set the frame visible
        setVisible(true);
    }

    private void defineFontes() {
        calcularButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.PLAIN, 20));
        changeThemeButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.PLAIN, 16));
        compararSimulacoesButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.PLAIN, 16));
        downloadExcelButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.PLAIN, 16));
    }

    private void defineOpcoesSistemasAmortizacao() {
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.PRICE.getNome());
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.SAC.getNome());
    }

    private void criarGrafico() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        simulationsMap.forEach((number, sistemaAmortizacao) -> {
            XYSeries series = new XYSeries(sistemaAmortizacao.getNomeFinanciamento());

            List<String> tabela = sistemaAmortizacao.getTabela();
            //Starts at 1 to skip header in priceTable
            for (int i = 1; i < tabela.size(); i++) {
                String[] row = tabela.get(i).split(",");
                series.add(Double.parseDouble(row[0]), Double.parseDouble(row[4]));
            }

            dataset.addSeries(series);
        });

        JFreeChart jFreeChart = ChartFactory.createXYLineChart("Comparação das simulações", "Número de Parcelas", "Saldo Devedor", dataset);
        ChartPanel chartPanel = new ChartPanel(jFreeChart, true, true, true, true, true);

        JFrame dialogFrame = new JFrame();
        dialogFrame.setMinimumSize(new Dimension(1100, 700));
        dialogFrame.setTitle("Comparador de simulações");
        dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialogFrame.setContentPane(chartPanel);
        dialogFrame.pack();
        dialogFrame.setLocationRelativeTo(null);
        dialogFrame.setVisible(true);
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;

        try {
            if (darkTheme) {
                UIManager.setLookAndFeel(new FlatArcDarkOrangeIJTheme());
            } else {
                UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
            }
        } catch (UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }

        SwingUtilities.updateComponentTreeUI(this);
        pack();
    }

    private void calcularFinanciamento() {

        SistemaAmortizacao sistemaAmortizacao = getSistemaAmortizacao((String) sistemaAmortizacaoComboBox.getSelectedItem());

//        SistemaAmortizacao sistemaAmortizacao = new PRICE("SBPE",
//                200_000.0, 30.0, 10.16, 1250.91,
//                350.0, 100.0, 350.0,
//                new RendimentoPassivo(40_000.0, 9.3), new FGTS(0.0, 0.0));

        sistemaAmortizacao.calcularFinanciamento();

        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas: " + sistemaAmortizacao.getNumeroParcelas());
        ratioValorPagoTotal.setText(String.format("Valor Imovel / Valor Pago Total: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));

        var jTable = getjTable(sistemaAmortizacao);
        JScrollPane scrollPane = new JScrollPane(jTable);

        tabs.add(nomeFinanciamento.getText(), scrollPane);
        tabs.setSelectedIndex(tabs.getTabCount() - 1);

        simulationsMap.put(tabs.getSelectedIndex(), sistemaAmortizacao);

        if (resetarCamposBox.isSelected()) {
            resetFields();
        }
    }

    private JTable getjTable(SistemaAmortizacao sistemaAmortizacao) {
        // Column Names
        final String[] columnNames = Constants.COLUMN_NAMES;
        List<String> tabela = sistemaAmortizacao.getTabela();
        List<TableRow> linhasTabela = sistemaAmortizacao.getLinhasTabela();

        //Starts at 1 to skip header
        String[][] data = new String[tabela.size() - 1][columnNames.length];
        for (int i = 1; i < tabela.size(); i++) {
            //Usando a tabela de string
            String[] row = tabela.get(i).split(",");
            data[i - 1] = row;
        }

//        String[][] data = new String[linhasTabela.size()][columnNames.length];
//        for (int i = 0; i < linhasTabela.size(); i++) {
//            //Usando a tabela tipada
//            TableRow tableRow = linhasTabela.get(i);
//            String[] row = new String[columnNames.length];
//            row[0] = String.valueOf(tableRow.getNumeroParcelas());
//            row[1] = String.valueOf(tableRow.getValorParcela());
//            row[2] = String.valueOf(tableRow.getValorExtra());
//            row[3] = String.valueOf(tableRow.getValorPagoMensal());
//            row[4] = String.valueOf(tableRow.getSaldoDevedor());
//            data[i] = row;
//        }

//        priceJTable.getTableHeader().setPreferredSize(new Dimension(100, 30));
//        priceJTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 22));
//        priceJTable.setRowHeight(26);

        Class[] types = new Class[columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            types[i] = String.class;
        }

        var jTable = new JTable();
        jTable.setShowHorizontalLines(true);
        jTable.setShowVerticalLines(true);
        jTable.setModel(
                new DefaultTableModel(data, columnNames) {

                    final boolean[] canEdit = new boolean[]{false, false, false, false, false, false};

                    @Override
                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                }
        );

        jTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable.setDefaultRenderer(String.class, centerRenderer);
        return jTable;
    }

    private SistemaAmortizacao getSistemaAmortizacao(String sistemaSelecionado) {
        SistemaAmortizacao sistemaAmortizacao;
        if (SistemaAmortizacaoEnum.PRICE.getNome().equals(sistemaSelecionado)) {
            sistemaAmortizacao = new PRICE(
                    nomeFinanciamento.getText(),
                    Double.valueOf(valorImovel.getText()),
                    (double) percentualEntrada.getValue(),
                    Double.valueOf(jurosAnual.getText()),
                    Integer.valueOf(prazo.getText()),
                    Double.valueOf(valorExtraInicial.getText()),
                    Double.valueOf(percentProximoValorExtra.getText()),
                    Double.valueOf(valorExtraMinimo.getText()),
                    new RendimentoPassivo(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText())),
                    new FGTS(
                            Double.valueOf(saldoFGTS.getText()),
                            Double.valueOf(salarioBruto.getText())
                    )
            );
        } else {
            sistemaAmortizacao = new SAC(
                    nomeFinanciamento.getText(),
                    Double.valueOf(valorImovel.getText()),
                    (double) percentualEntrada.getValue(),
                    Double.valueOf(jurosAnual.getText()),
                    Integer.valueOf(prazo.getText()),
                    Double.valueOf(valorExtraInicial.getText()),
                    Double.valueOf(percentProximoValorExtra.getText()),
                    Double.valueOf(valorExtraMinimo.getText()),
                    new RendimentoPassivo(
                            Double.valueOf(valorInvestido.getText()),
                            Double.valueOf(rendimentoAnual.getText())),
                    new FGTS(
                            Double.valueOf(saldoFGTS.getText()),
                            Double.valueOf(salarioBruto.getText())
                    )
            );
        }
        return sistemaAmortizacao;
    }

    private void resetFields() {
        nomeFinanciamento.setText("");
        valorImovel.setText("");
        percentualEntrada.setValue(50);
        jurosAnual.setText("");
        prazo.setText("");
        valorExtraInicial.setText("");
        percentProximoValorExtra.setText("");
        valorExtraMinimo.setText("");
        valorInvestido.setText("");
        rendimentoAnual.setText("");
    }

    private void updateFields(SistemaAmortizacao sistemaAmortizacao) {
        nomeFinanciamento.setText(sistemaAmortizacao.getNomeFinanciamento());
        valorImovel.setText(sistemaAmortizacao.getValorImovel().toString());
        percentualEntrada.setValue((int) (sistemaAmortizacao.getPercentualEntrada() * 100));
        sistemaAmortizacaoComboBox.setSelectedIndex(sistemaAmortizacao.getSistemaAmortizacao().getIndex());
        jurosAnual.setText(String.valueOf(sistemaAmortizacao.getJurosAnual() * 100));
        valorExtraInicial.setText(sistemaAmortizacao.getValorExtraInicial().toString());
        percentProximoValorExtra.setText(String.valueOf(sistemaAmortizacao.getPercentProximoValorExtra() * 100));
        valorExtraMinimo.setText(sistemaAmortizacao.getValorExtraMinimo().toString());
        valorInvestido.setText(sistemaAmortizacao.getRendimentoPassivo().getValorInvestido().toString());
        rendimentoAnual.setText(sistemaAmortizacao.getRendimentoPassivo().getRendimentoAnual().toString());

        //Bottom Fields
        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas: " + sistemaAmortizacao.getNumeroParcelas());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setEnabled(true);
        contentPane.setPreferredSize(new Dimension(1400, 800));
        verticalSplitPlane = new JSplitPane();
        verticalSplitPlane.setOrientation(1);
        contentPane.add(verticalSplitPlane, new GridConstraints(0, 0, 10, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        leftPanel.setForeground(new Color(-13947600));
        leftPanel.setMinimumSize(new Dimension(450, 673));
        verticalSplitPlane.setLeftComponent(leftPanel);
        horizontalSplitPane = new JSplitPane();
        horizontalSplitPane.setOrientation(0);
        leftPanel.add(horizontalSplitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, -1), new Dimension(400, -1), null, 0, false));
        bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new GridLayoutManager(7, 2, new Insets(20, 20, 20, 20), -1, -1));
        horizontalSplitPane.setRightComponent(bottomLeftPanel);
        valorPagoTotalField = new JLabel();
        valorPagoTotalField.setText("Valor Pago Total:");
        bottomLeftPanel.add(valorPagoTotalField, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numeroParcelasField = new JLabel();
        numeroParcelasField.setText("Número de Parcelas:");
        bottomLeftPanel.add(numeroParcelasField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), new Dimension(-1, 20), 0, false));
        ratioValorPagoTotal = new JLabel();
        ratioValorPagoTotal.setText("Valor Pago Total / Valor Imovel:");
        ratioValorPagoTotal.setToolTipText(" ");
        bottomLeftPanel.add(ratioValorPagoTotal, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomLeftPanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        changeThemeButton = new JButton();
        Font changeThemeButtonFont = this.$$$getFont$$$(null, -1, -1, changeThemeButton.getFont());
        if (changeThemeButtonFont != null) changeThemeButton.setFont(changeThemeButtonFont);
        changeThemeButton.setText("Alterar Tema");
        bottomLeftPanel.add(changeThemeButton, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        compararSimulacoesButton = new JButton();
        Font compararSimulacoesButtonFont = this.$$$getFont$$$(null, -1, -1, compararSimulacoesButton.getFont());
        if (compararSimulacoesButtonFont != null) compararSimulacoesButton.setFont(compararSimulacoesButtonFont);
        compararSimulacoesButton.setText("Comparar Simulações");
        bottomLeftPanel.add(compararSimulacoesButton, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        downloadExcelButton = new JButton();
        downloadExcelButton.setText("Download Excel");
        downloadExcelButton.setVerticalTextPosition(3);
        bottomLeftPanel.add(downloadExcelButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        upperLeftPanel = new JPanel();
        upperLeftPanel.setLayout(new GridLayoutManager(16, 1, new Insets(20, 20, 20, 20), -1, -1));
        upperLeftPanel.setFocusable(false);
        upperLeftPanel.setForeground(new Color(-13947600));
        horizontalSplitPane.setLeftComponent(upperLeftPanel);
        nomeFinanciamentoPanel = new JPanel();
        nomeFinanciamentoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(nomeFinanciamentoPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeFinanciamentoLabel = new JLabel();
        nomeFinanciamentoLabel.setText("Nome Financiamento");
        nomeFinanciamentoPanel.add(nomeFinanciamentoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        nomeFinanciamento = new JTextField();
        nomeFinanciamentoPanel.add(nomeFinanciamento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorImovelPanel = new JPanel();
        valorImovelPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorImovelPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorImovelLabel = new JLabel();
        valorImovelLabel.setText("Valor Imóvel");
        valorImovelPanel.add(valorImovelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorImovel = new JTextField();
        valorImovelPanel.add(valorImovel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualEntradaPanel = new JPanel();
        percentualEntradaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualEntradaPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentualEntradaLabel = new JLabel();
        percentualEntradaLabel.setText("Percentual Entrada");
        percentualEntradaPanel.add(percentualEntradaLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentualEntrada = new JSlider();
        percentualEntrada.setMajorTickSpacing(10);
        percentualEntrada.setMinorTickSpacing(10);
        percentualEntrada.setPaintLabels(true);
        percentualEntrada.setPaintTicks(false);
        percentualEntrada.setPaintTrack(true);
        percentualEntrada.setSnapToTicks(true);
        percentualEntrada.setValueIsAdjusting(false);
        percentualEntradaPanel.add(percentualEntrada, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jurosAnualPanel = new JPanel();
        jurosAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(jurosAnualPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        jurosAnualLabel = new JLabel();
        jurosAnualLabel.setText("Juros Anual");
        jurosAnualPanel.add(jurosAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        jurosAnual = new JTextField();
        jurosAnualPanel.add(jurosAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraInicialPanel = new JPanel();
        valorExtraInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorExtraInicialPanel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorExtraInicialLabel = new JLabel();
        valorExtraInicialLabel.setText("Valor Extra Inicial");
        valorExtraInicialPanel.add(valorExtraInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraInicial = new JTextField();
        valorExtraInicialPanel.add(valorExtraInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualProximoValorExtraPanel = new JPanel();
        percentualProximoValorExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(percentualProximoValorExtraPanel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentProximoValorExtraLabel = new JLabel();
        percentProximoValorExtraLabel.setText("Perc. Prox. Valor Extra");
        percentProximoValorExtraLabel.putClientProperty("html.disable", Boolean.FALSE);
        percentualProximoValorExtraPanel.add(percentProximoValorExtraLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentProximoValorExtra = new JTextField();
        percentualProximoValorExtraPanel.add(percentProximoValorExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraMinimoPanel = new JPanel();
        valorExtraMinimoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorExtraMinimoPanel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, -1), null, 0, false));
        valorExtraMinimoLabel = new JLabel();
        valorExtraMinimoLabel.setText("Valor Extra Mínimo");
        valorExtraMinimoPanel.add(valorExtraMinimoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraMinimo = new JTextField();
        valorExtraMinimoPanel.add(valorExtraMinimo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorInvestidoPanel = new JPanel();
        valorInvestidoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(valorInvestidoPanel, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorInvestidoLabel = new JLabel();
        valorInvestidoLabel.setText("Valor Investido");
        valorInvestidoPanel.add(valorInvestidoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorInvestido = new JTextField();
        valorInvestidoPanel.add(valorInvestido, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rendimentoAnualPanel = new JPanel();
        rendimentoAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(rendimentoAnualPanel, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rendimentoAnualLabel = new JLabel();
        rendimentoAnualLabel.setText("Rendimento Anual");
        rendimentoAnualPanel.add(rendimentoAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        rendimentoAnual = new JTextField();
        rendimentoAnualPanel.add(rendimentoAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        upperLeftPanel.add(spacer2, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 50), null, 0, false));
        calcularButton = new JButton();
        calcularButton.setEnabled(true);
        Font calcularButtonFont = this.$$$getFont$$$(null, -1, -1, calcularButton.getFont());
        if (calcularButtonFont != null) calcularButton.setFont(calcularButtonFont);
        calcularButton.setHideActionText(false);
        calcularButton.setHorizontalTextPosition(0);
        calcularButton.setText("Calcular");
        upperLeftPanel.add(calcularButton, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        salarioBrutoPanel = new JPanel();
        salarioBrutoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(salarioBrutoPanel, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Salário Bruto");
        salarioBrutoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        salarioBruto = new JTextField();
        salarioBrutoPanel.add(salarioBruto, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoAtualPanel = new JPanel();
        saldoAtualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(saldoAtualPanel, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Saldo FGTS");
        saldoAtualPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        saldoFGTS = new JTextField();
        saldoAtualPanel.add(saldoFGTS, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resetarCamposPanel = new JPanel();
        resetarCamposPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(resetarCamposPanel, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Resetar campos?");
        resetarCamposPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resetarCamposBox = new JCheckBox();
        resetarCamposBox.setText("");
        resetarCamposPanel.add(resetarCamposBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        escolherSistemaPanel = new JPanel();
        escolherSistemaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(escolherSistemaPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sistema Amortização");
        escolherSistemaPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        sistemaAmortizacaoComboBox = new JComboBox();
        escolherSistemaPanel.add(sistemaAmortizacaoComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        prazoPanel = new JPanel();
        prazoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        upperLeftPanel.add(prazoPanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prazoLabel = new JLabel();
        prazoLabel.setText("Prazo (meses)");
        prazoPanel.add(prazoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        prazo = new JTextField();
        prazoPanel.add(prazo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        verticalSplitPlane.setRightComponent(rightPanel);
        tabs = new JTabbedPane();
        rightPanel.add(tabs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
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
        return contentPane;
    }

}
