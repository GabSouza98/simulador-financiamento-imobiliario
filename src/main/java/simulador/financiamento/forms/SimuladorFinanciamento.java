package simulador.financiamento.forms;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import simulador.financiamento.dominio.Financiamento;
import simulador.financiamento.dominio.OpcoesAvancadas;
import simulador.financiamento.dominio.amortizacao.AmortizacaoExtra;
import simulador.financiamento.dominio.amortizacao.FGTS;
import simulador.financiamento.dominio.amortizacao.Investimentos;
import simulador.financiamento.dominio.amortizacao.Recorrencia;
import simulador.financiamento.dominio.enums.AcoesResgate;
import simulador.financiamento.dominio.enums.SistemaAmortizacaoEnum;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacao;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacaoFactory;
import simulador.financiamento.tabela.ExcelWriter;
import simulador.financiamento.utils.JTabbedPaneCloseButton;
import simulador.financiamento.utils.SwingUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static simulador.financiamento.utils.Constants.*;

public class SimuladorFinanciamento extends JFrame {

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
    private JPanel escolherSistemaPanel;
    private JComboBox<String> sistemaAmortizacaoComboBox;
    private JPanel prazoPanel;
    private JLabel prazoLabel;
    private JTextField prazo;
    private JButton advancedOptionsButton;
    private JPanel mesInicialPanel;
    private JTextField mesInicial;
    private JLabel mesInicialLabel;
    private JPanel intervaloPanel;
    private JTextField intervalo;
    private JLabel intervaloLabel;
    private JPanel quantidadePanel;
    private JTextField repeticoes;
    private JLabel recorrencia;
    private JLabel investimentosLabel;
    private JLabel fgtsLabel;
    private JCheckBox recorrenciaCheckBox;
    private JCheckBox investimentosCheckBox;
    private JCheckBox fgtsCheckBox;
    private JPanel amortizacaoExtraPanel;
    private JPanel investimentosPanel;
    private JPanel fgtsPanel;
    private JCheckBox sempreCheckBox;
    private JLabel anosConclusaoField;
    private JLabel valorImovelValorizado;
    private JLabel valorImovelInflacao;
    private JButton saveButton;
    private JButton loadButton;
    private JPanel resgatePanel;
    private JLabel resgateLabel;
    private JTextField resgate;
    private JLabel acaoNoResgate;
    private JComboBox<String> resgateComboBox;
    private JPanel acaoResgatePanel;
    private JLabel saldoInvestido;
    private JLabel valorizacaoReal;
    private JLabel repeticoesLabel;
    private JButton resetarCamposButton;

    private final ExcelWriter excelWriter = new ExcelWriter();

    private boolean darkTheme = true;

    private ArrayList<SistemaAmortizacao> simulationsList = new ArrayList<>();

    private OpcoesAvancadas opcoesAvancadas = new OpcoesAvancadas(0.0, 0.0);

    public SimuladorFinanciamento() {
        $$$setupUI$$$();
        setTitle("Simulador de Financiamentos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        getContentPane().setBackground(Color.black);

        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(10000);

        JScrollPane jScrollPane = new JScrollPane(upperLeftPanel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        horizontalSplitPane.setLeftComponent(jScrollPane);
        horizontalSplitPane.setDividerLocation((int) (contentPane.getPreferredSize().getHeight() * 0.6));

        pack();
        // Set the frame location to the center of the screen
        setLocationRelativeTo(null);

        defineOpcoesSistemasAmortizacao();
        defineOpcoesResgate();
        defineFontes();
        desabilitaCamposRecorrencia();
        desabilitaCamposInvestimentos();
        desabilitaCamposFgts();
        preencheCamposOpcionaisDefault();

        calcularButton.addActionListener(e -> calcularFinanciamento());
        advancedOptionsButton.addActionListener(e -> abrirOpcoesAvancadas());
        resetarCamposButton.addActionListener(e -> resetFields());
        changeThemeButton.addActionListener(e -> toggleTheme());
        compararSimulacoesButton.addActionListener(e -> compararSimulacoes(simulationsList));
        downloadExcelButton.addActionListener(e -> fazerDownload());
        saveButton.addActionListener(e -> salvar());
        loadButton.addActionListener(e -> carregar());

        tabs.addChangeListener(e -> {
            if (simulationsList.isEmpty()) {
                resetFields();
            }

            if (!simulationsList.isEmpty() && simulationsList.size() == tabs.getTabCount()) {
                int selectedTab = tabs.getSelectedIndex();
                SistemaAmortizacao sistemaAmortizacao = simulationsList.get(selectedTab);
                updateFields(sistemaAmortizacao);
            }
        });

        recorrenciaCheckBox.addActionListener(e -> {
            if (recorrenciaCheckBox.isSelected()) {
                habilitaCamposRecorrencia();
            } else {
                desabilitaCamposRecorrencia();
            }
        });

        investimentosCheckBox.addActionListener(e -> {
            if (investimentosCheckBox.isSelected()) {
                habilitaCamposInvestimentos();
            } else {
                desabilitaCamposInvestimentos();
            }
        });

        fgtsCheckBox.addActionListener(e -> {
            if (fgtsCheckBox.isSelected()) {
                habilitaCamposFgts();
            } else {
                desabilitaCamposFgts();
            }
        });

        sempreCheckBox.addActionListener(e -> {
            repeticoes.setEnabled(!sempreCheckBox.isSelected());
        });

        sempreCheckBox.setSelected(false);
        repeticoes.setEnabled(true);

        // Set the frame visible
        setVisible(true);
    }

    private void abrirOpcoesAvancadas() {
        OpcoesAvancadasDialog dialog = new OpcoesAvancadasDialog(opcoesAvancadas);
        dialog.setMinimumSize(new Dimension(400, 200));
        dialog.setTitle("Opções Avançadas");
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.pack();

        opcoesAvancadas = dialog.showDialog();
        System.out.println(opcoesAvancadas);
    }

    private void salvar() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("simulationsList.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(simulationsList);
            objectOutputStream.close();
            fileOutputStream.close();

            new InformacaoDialog("Dados Salvos", "Dados salvos com sucesso!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void carregar() {
        try {
            FileInputStream fileInput = new FileInputStream("simulationsList.txt");
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            simulationsList = (ArrayList<SistemaAmortizacao>) objectInput.readObject();

            objectInput.close();
            fileInput.close();

            tabs.removeAll();

            simulationsList.forEach((sistemaAmortizacao) -> {
                JTable jTable = SwingUtils.getSimulationJTable(sistemaAmortizacao);
                JScrollPane scrollPane = new JScrollPane(jTable);
                tabs.add(sistemaAmortizacao.getNomeFinanciamento(), scrollPane);
            });

            tabs.setSelectedIndex(tabs.getTabCount() - 1);

            ((JTabbedPaneCloseButton) tabs).setSimulationsList(simulationsList);

        } catch (FileNotFoundException e) {
            new InformacaoDialog("Erro ao carregar", "O sistema não pode encontrar o arquivo especificado.");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fazerDownload() {
        if (tabs.getTabCount() > 0) {
            int selectedTab = tabs.getSelectedIndex();
            SistemaAmortizacao sistemaAmortizacao = simulationsList.get(selectedTab);
            excelWriter.writeTable(sistemaAmortizacao);

            new InformacaoDialog("Excel Salvo", "Excel salvo com sucesso!");
        }
    }

    private void defineFontes() {
        calcularButton.setFont(new Font(calcularButton.getFont().getFontName(), Font.BOLD, 16));
        resetarCamposButton.setFont(new Font(resetarCamposButton.getFont().getFontName(), Font.PLAIN, 16));
        changeThemeButton.setFont(new Font(changeThemeButton.getFont().getFontName(), Font.PLAIN, 16));
        compararSimulacoesButton.setFont(new Font(compararSimulacoesButton.getFont().getFontName(), Font.BOLD, 16));
        advancedOptionsButton.setFont(new Font(advancedOptionsButton.getFont().getFontName(), Font.PLAIN, 16));
        downloadExcelButton.setFont(new Font(downloadExcelButton.getFont().getFontName(), Font.PLAIN, 16));
        saveButton.setFont(new Font(saveButton.getFont().getFontName(), Font.PLAIN, 16));
        loadButton.setFont(new Font(loadButton.getFont().getFontName(), Font.PLAIN, 16));
    }

    private void defineOpcoesSistemasAmortizacao() {
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.PRICE.getNome());
        sistemaAmortizacaoComboBox.addItem(SistemaAmortizacaoEnum.SAC.getNome());
    }

    private void defineOpcoesResgate() {
        resgateComboBox.addItem(AcoesResgate.AMORTIZAR.getNome());
        resgateComboBox.addItem(AcoesResgate.REINVESTIR.getNome());
    }

    private void desabilitaCamposRecorrencia() {
        valorExtraInicialPanel.setVisible(false);
        percentualProximoValorExtraPanel.setVisible(false);
        valorExtraMinimoPanel.setVisible(false);
        mesInicialPanel.setVisible(false);
        intervaloPanel.setVisible(false);
        quantidadePanel.setVisible(false);
    }

    private void habilitaCamposRecorrencia() {
        valorExtraInicialPanel.setVisible(true);
        percentualProximoValorExtraPanel.setVisible(true);
        valorExtraMinimoPanel.setVisible(true);
        mesInicialPanel.setVisible(true);
        intervaloPanel.setVisible(true);
        quantidadePanel.setVisible(true);
    }

    private void desabilitaCamposInvestimentos() {
        valorInvestidoPanel.setVisible(false);
        rendimentoAnualPanel.setVisible(false);
        resgatePanel.setVisible(false);
        acaoResgatePanel.setVisible(false);
    }

    private void habilitaCamposInvestimentos() {
        valorInvestidoPanel.setVisible(true);
        rendimentoAnualPanel.setVisible(true);
        resgatePanel.setVisible(true);
        acaoResgatePanel.setVisible(true);
    }

    private void desabilitaCamposFgts() {
        salarioBrutoPanel.setVisible(false);
        saldoAtualPanel.setVisible(false);
    }

    private void habilitaCamposFgts() {
        salarioBrutoPanel.setVisible(true);
        saldoAtualPanel.setVisible(true);
    }

    private void preencheCamposOpcionaisDefault() {
        defaultRecorrencia();
        defaultInvestimentos();
        defaultFGTS();
    }

    private void defaultRecorrencia() {
        valorExtraInicial.setText("0");
        percentProximoValorExtra.setText("100");
        valorExtraMinimo.setText("0");
        mesInicial.setText("1");
        intervalo.setText("1");
        repeticoes.setText("0");
    }

    private void defaultInvestimentos() {
        valorInvestido.setText("0");
        rendimentoAnual.setText("0");
        resgate.setText("2");
        resgateComboBox.setSelectedIndex(0);
    }

    private void defaultFGTS() {
        salarioBruto.setText("0");
        saldoFGTS.setText("0");
    }

    private void compararSimulacoes(ArrayList<SistemaAmortizacao> simulationsList) {
        new CompararSimulacoes(simulationsList);
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

        if (!validateJTextField() ||
            !validateIntegerFields() ||
            !validateValorImovel() ||
            !validateTaxaJuros() ||
            !validatePrazo() ||
            !validateRecorrencia() ||
            !validateProxValorExtra() ||
            !validateMesInicial() ||
            !validateIntervalo() ||
            !validateInvestimentos() ||
            !validateResgate() ||
            !validateFGTS() ||
            !validateDuplicatedSimulationName()) {
            return;
        }

        SistemaAmortizacao sistemaAmortizacao = getSistemaAmortizacao((String) sistemaAmortizacaoComboBox.getSelectedItem());
        sistemaAmortizacao.calcularFinanciamento();

        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %,.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas / Prazo: " + sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        valorizacaoReal.setText(String.format("Valorização Real: R$ %,.2f", sistemaAmortizacao.getOpcoesAvancadas().getValorImovelValorizado() - sistemaAmortizacao.getOpcoesAvancadas().getValorImovelInflacao()));
        saldoInvestido.setText(String.format("Saldo Valor Investido: R$ %,.2f", AmortizacaoExtra.buscarAmortizacaoExtra(Investimentos.class, sistemaAmortizacao.getAmortizacaoExtraList()).getValorInvestido()));

        JTable jTable = SwingUtils.getSimulationJTable(sistemaAmortizacao);
        JScrollPane scrollPane = new JScrollPane(jTable);

        simulationsList.add(sistemaAmortizacao);

        tabs.add(nomeFinanciamento.getText(), scrollPane);
        tabs.setSelectedIndex(tabs.getTabCount() - 1);
    }

    private SistemaAmortizacao getSistemaAmortizacao(String sistemaSelecionado) {

        Financiamento financiamento = new Financiamento(
                nomeFinanciamento.getText(),
                Double.valueOf(sanitize(valorImovel.getText())),
                (double) percentualEntrada.getValue(),
                Double.valueOf(sanitize(jurosAnual.getText())),
                Integer.valueOf(sanitize(prazo.getText()))
        );

        //Se os campos não estiverem selecionados, não serão considerados.
        if (!recorrenciaCheckBox.isSelected()) {
            defaultRecorrencia();
        }

        if (!investimentosCheckBox.isSelected()) {
            defaultInvestimentos();
        }

        if (!fgtsCheckBox.isSelected()) {
            defaultFGTS();
        }

        Recorrencia recorrencia = new Recorrencia(
                Double.valueOf(sanitize(valorExtraInicial.getText())),
                Double.valueOf(sanitize(percentProximoValorExtra.getText())),
                Double.valueOf(sanitize(valorExtraMinimo.getText())),
                Integer.valueOf(sanitize(mesInicial.getText())),
                Integer.valueOf(sanitize(intervalo.getText())),
                Integer.valueOf(sanitize(repeticoes.getText())),
                sempreCheckBox.isSelected()
        );

        Investimentos investimentos = new Investimentos(
                Double.valueOf(sanitize(valorInvestido.getText())),
                Double.valueOf(sanitize(rendimentoAnual.getText())),
                Integer.valueOf(sanitize(resgate.getText())),
                (String) resgateComboBox.getSelectedItem()
        );

        FGTS fgts = new FGTS(
                Double.valueOf(sanitize(saldoFGTS.getText())),
                Double.valueOf(sanitize(salarioBruto.getText()))
        );

        OpcoesAvancadas opcoesAvancadas = OpcoesAvancadas.from(this.opcoesAvancadas);

        SistemaAmortizacaoEnum sistema = SistemaAmortizacaoEnum.getByName(sistemaSelecionado);

        return SistemaAmortizacaoFactory.getSistemaAmortizacao(
                sistema,
                financiamento,
                recorrencia,
                investimentos,
                fgts,
                opcoesAvancadas
        );
    }

    private void resetFields() {
        nomeFinanciamento.setText("");
        valorImovel.setText("");
        percentualEntrada.setValue(50);
        sistemaAmortizacaoComboBox.setSelectedIndex(0);
        jurosAnual.setText("");
        prazo.setText("");

        preencheCamposOpcionaisDefault();

        recorrenciaCheckBox.setSelected(false);
        investimentosCheckBox.setSelected(false);
        fgtsCheckBox.setSelected(false);

        desabilitaCamposRecorrencia();
        desabilitaCamposInvestimentos();
        desabilitaCamposFgts();
    }

    private void updateFields(SistemaAmortizacao sistemaAmortizacao) {
        nomeFinanciamento.setText(sistemaAmortizacao.getNomeFinanciamento());
        valorImovel.setText(decimalFormatter.format(sistemaAmortizacao.getValorImovel()));
        percentualEntrada.setValue((int) (sistemaAmortizacao.getPercentualEntrada() * 100));
        sistemaAmortizacaoComboBox.setSelectedIndex(sistemaAmortizacao.getSistemaAmortizacao().getIndex());
        jurosAnual.setText(decimalFormatter.format(sistemaAmortizacao.getJurosAnual()));
        prazo.setText(String.valueOf(sistemaAmortizacao.getPrazo()));

        Recorrencia recorrencia = AmortizacaoExtra.buscarAmortizacaoExtra(Recorrencia.class, sistemaAmortizacao.getAmortizacaoExtraList());
        valorExtraInicial.setText(decimalFormatter.format(recorrencia.getValorExtraInicial()));
        percentProximoValorExtra.setText(decimalFormatter.format(recorrencia.getPercentProximoValorExtra() * 100));
        valorExtraMinimo.setText(decimalFormatter.format(recorrencia.getValorExtraMinimo()));
        mesInicial.setText(recorrencia.getMesInicial().toString());
        intervalo.setText(recorrencia.getIntervalo().toString());
        repeticoes.setText(recorrencia.getQuantidadeAmortizacoesDesejadas().toString());
        sempreCheckBox.setSelected(recorrencia.getSemLimiteAmortizacoes());

        //Marca o campo caso tenha valor
        if (recorrencia.getValorExtra() > 0) {
            recorrenciaCheckBox.setSelected(true);
            habilitaCamposRecorrencia();
        } else {
            recorrenciaCheckBox.setSelected(false);
            desabilitaCamposRecorrencia();
        }

        Investimentos investimentos = AmortizacaoExtra.buscarAmortizacaoExtra(Investimentos.class, sistemaAmortizacao.getAmortizacaoExtraList());
        valorInvestido.setText(decimalFormatter.format(investimentos.getValorInicialPrimeiroAporte()));
        rendimentoAnual.setText(decimalFormatter.format(investimentos.getRendimentoAnual()));
        resgate.setText(investimentos.getPrazoResgateAnos().toString());
        resgateComboBox.setSelectedIndex(investimentos.getAcao().getIndex());

        if (investimentos.getValorInicialPrimeiroAporte() > 0) {
            investimentosCheckBox.setSelected(true);
            habilitaCamposInvestimentos();
        } else {
            investimentosCheckBox.setSelected(false);
            desabilitaCamposInvestimentos();
        }

        FGTS fgts = AmortizacaoExtra.buscarAmortizacaoExtra(FGTS.class, sistemaAmortizacao.getAmortizacaoExtraList());
        salarioBruto.setText(decimalFormatter.format(fgts.getSalario()));
        saldoFGTS.setText(decimalFormatter.format(fgts.getSaldoInicial()));

        if (fgts.getSalario() > 0) {
            fgtsCheckBox.setSelected(true);
            habilitaCamposFgts();
        } else {
            fgtsCheckBox.setSelected(false);
            desabilitaCamposFgts();
        }

        OpcoesAvancadas opcoesAvanc = sistemaAmortizacao.getOpcoesAvancadas();

        //Bottom Fields
        valorPagoTotalField.setText(String.format("Valor Pago Total: R$ %,.2f", sistemaAmortizacao.getValorPagoTotal()));
        numeroParcelasField.setText("Número de Parcelas / Prazo: " + sistemaAmortizacao.getNumeroParcelas() + "/" + sistemaAmortizacao.getPrazo());
        ratioValorPagoTotal.setText(String.format("Valor Pago Total / Valor Imovel: %.2f", sistemaAmortizacao.getValorPagoTotal() / sistemaAmortizacao.getValorImovel()));
        anosConclusaoField.setText(String.format("Quitado em: %.1f anos", (double) sistemaAmortizacao.getNumeroParcelas() / 12));
        valorImovelValorizado.setText(String.format("Valor Imóvel Valorizado: R$ %,.2f", opcoesAvanc.getValorImovelValorizado()));
        valorImovelInflacao.setText(String.format("Valor Imóvel Inflação: R$ %,.2f", opcoesAvanc.getValorImovelInflacao()));
        valorizacaoReal.setText(String.format("Valorização Real: R$ %,.2f", opcoesAvanc.getValorImovelValorizado() - opcoesAvanc.getValorImovelInflacao()));
        saldoInvestido.setText(String.format("Saldo Valor Investido: R$ %,.2f", investimentos.getValorInvestido()));

        opcoesAvancadas = opcoesAvanc;
    }

    private boolean validateJTextField() {
        Field[] fields = this.getClass().getDeclaredFields();

        var textFields = Arrays.stream(fields)
                .filter(f -> JTextField.class.equals(f.getType()))
                .collect(Collectors.toList());

        boolean valid = true;

        try {
            for (Field f : textFields) {

                f.setAccessible(true);
                JTextField textField = (JTextField) f.get(this);

                if (textField.getText().isBlank()) {
                    if (f.getName().equals("nomeFinanciamento") ||
                        f.getName().equals("valorImovel") ||
                        f.getName().equals("jurosAnual") ||
                        f.getName().equals("prazo")) {

                        JOptionPane.showMessageDialog(null,
                                "Nenhum campo obrigatório pode ficar em branco." +
                                        "\nCampo em branco: " + f.getName() + ".",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    } else {
                        textField.setText("0");
                    }
                }

                if (f.getName().equals("nomeFinanciamento")) {
                    continue;
                }

                if (!pattern.matcher(textField.getText()).matches()) {
                    JOptionPane.showMessageDialog(null,
                            "O campo \"" + f.getName() + "\" não respeita o formato \"123.456,78\"." +
                                    "\nUse \",\" como separador decimal e \".\" como separador de milhar (opcional).",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                double number = Double.parseDouble(sanitize(textField.getText()));
                if (number < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Nenhum campo pode ser negativo. Campo negativo: " + f.getName() + ". Valor: " + textField.getText(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (NumberFormatException | IllegalAccessException e) {
            valid = false;
        }

        return valid;
    }

    private boolean validateIntegerFields() {
        ArrayList<JTextField> jTextFieldList = new ArrayList<>();
        jTextFieldList.add(prazo);
        jTextFieldList.add(mesInicial);
        jTextFieldList.add(intervalo);
        jTextFieldList.add(repeticoes);
        jTextFieldList.add(resgate);

        for (JTextField jTextField : jTextFieldList) {
            try {
                Integer.parseInt(sanitize(jTextField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "O campo deve ser um número inteiro. Valor atual: " + jTextField.getText(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private boolean validateTaxaJuros() {
        double number = Double.parseDouble(sanitize(jurosAnual.getText()));

        if (number <= 0) {
            JOptionPane.showMessageDialog(null,
                    "A taxa de júros deve ser maior que zero. Valor atual: " + jurosAnual.getText(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validatePrazo() {
        double number = Double.parseDouble(sanitize(prazo.getText()));

        if (number <= 0) {
            JOptionPane.showMessageDialog(null,
                    "O prazo do financiamento deve ser maior que zero. Valor atual: " + prazo.getText(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateResgate() {
        if (Integer.parseInt(sanitize(resgate.getText())) < 2) {
            resgate.setText("2");
            JOptionPane.showMessageDialog(null,
                    "O tempo mínimo para resgate é de 2 anos.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateIntervalo() {
        if (Integer.parseInt(sanitize(intervalo.getText())) < 1) {
            intervalo.setText("1");
            JOptionPane.showMessageDialog(null,
                    "O intervalo entre pagamentos deve ser maior ou igual a 1.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateMesInicial() {
        if (Integer.parseInt(sanitize(mesInicial.getText())) < 1) {
            mesInicial.setText("1");
            JOptionPane.showMessageDialog(null,
                    "O mês inicial para amortizações extra deve ser maior ou igual a 1.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateProxValorExtra() {
        double number = Double.parseDouble(sanitize(percentProximoValorExtra.getText()));
        if (number < 0 || number > 100) {
            JOptionPane.showMessageDialog(null,
                    "O valor do campo Próx. Valor deve estar entre 0 e 100. Valor atual: " + percentProximoValorExtra.getText(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateDuplicatedSimulationName() {
        boolean duplicatedName = simulationsList.stream()
                .map(SistemaAmortizacao::getNomeFinanciamento)
                .anyMatch(name -> name.equals(nomeFinanciamento.getText()));

        if (duplicatedName) {
            JOptionPane.showMessageDialog(null,
                    "O nome da simulação já foi utilizado, favor informar um nome único.",
                    "Nome duplicado",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateValorImovel() {
        double number = Double.parseDouble(sanitize(valorImovel.getText()));

        if (number <= 0) {
            JOptionPane.showMessageDialog(null,
                    "O valor do imóvel deve ser maior que zero. Valor atual: " + valorImovel.getText(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateRecorrencia() {
        if (recorrenciaCheckBox.isSelected()) {
            if (Double.parseDouble(sanitize(valorExtraInicial.getText())) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "O campo \"Valor Inicial\" precisa ser maior que zero para a amortização ter efeito.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!sempreCheckBox.isSelected() && Integer.parseInt(sanitize(repeticoes.getText())) <= 0) {
                mesInicial.setText("1");
                JOptionPane.showMessageDialog(null,
                        "O campo \"Repetições\" precisa ser maior que zero ou o checkbox\n \"Sempre\" " +
                                "precisa estar selecionado para a amortização ter efeito.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private boolean validateInvestimentos() {
        if (investimentosCheckBox.isSelected()) {
            if (Double.parseDouble(sanitize(valorInvestido.getText())) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "O campo \"Valor Investido\" precisa ser maior que zero para a amortização ter efeito.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (Double.parseDouble(sanitize(rendimentoAnual.getText())) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "O campo \"Rendimento Anual\" precisa ser maior que zero para a amortização ter efeito.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private boolean validateFGTS() {
        if (fgtsCheckBox.isSelected()) {
            if (Double.parseDouble(sanitize(salarioBruto.getText())) <= 0) {
                JOptionPane.showMessageDialog(null,
                        "O campo \"Salário Bruto\" precisa ser maior que zero para a amortização ter efeito.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setEnabled(true);
        contentPane.setPreferredSize(new Dimension(1500, 900));
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
        bottomLeftPanel.setLayout(new GridLayoutManager(13, 3, new Insets(20, 20, 20, 20), -1, -1));
        bottomLeftPanel.setMaximumSize(new Dimension(2147483647, 300));
        bottomLeftPanel.setMinimumSize(new Dimension(239, 300));
        bottomLeftPanel.setPreferredSize(new Dimension(264, 300));
        horizontalSplitPane.setRightComponent(bottomLeftPanel);
        bottomLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        valorPagoTotalField = new JLabel();
        valorPagoTotalField.setText("Valor Pago Total:");
        valorPagoTotalField.setToolTipText("Soma do valor pago a cada mes até o final do financiamento.");
        bottomLeftPanel.add(valorPagoTotalField, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numeroParcelasField = new JLabel();
        numeroParcelasField.setText("Número de Parcelas / Prazo:");
        numeroParcelasField.setToolTipText("Número de parcelas pagas em relação ao prazo inicial contratado");
        bottomLeftPanel.add(numeroParcelasField, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), new Dimension(-1, 20), 0, false));
        ratioValorPagoTotal = new JLabel();
        ratioValorPagoTotal.setText("Valor Pago Total / Valor Imovel:");
        ratioValorPagoTotal.setToolTipText(" Relação entre valor pago e valor do imóvel na compra.");
        bottomLeftPanel.add(ratioValorPagoTotal, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        bottomLeftPanel.add(spacer1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        compararSimulacoesButton = new JButton();
        Font compararSimulacoesButtonFont = this.$$$getFont$$$(null, -1, -1, compararSimulacoesButton.getFont());
        if (compararSimulacoesButtonFont != null) compararSimulacoesButton.setFont(compararSimulacoesButtonFont);
        compararSimulacoesButton.setText("Comparar Simulações");
        compararSimulacoesButton.setToolTipText("Mostra tabela comparativa entre os resultados das simulações e outras opções para comparação gráfica.");
        bottomLeftPanel.add(compararSimulacoesButton, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        downloadExcelButton = new JButton();
        downloadExcelButton.setText("Download Excel");
        downloadExcelButton.setToolTipText("Realiza o download de um arquivo .xlsx contendo as informações do financiamento da aba atualmente selecionada.");
        downloadExcelButton.setVerticalTextPosition(3);
        bottomLeftPanel.add(downloadExcelButton, new GridConstraints(10, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        anosConclusaoField = new JLabel();
        anosConclusaoField.setText("Quitado em:");
        anosConclusaoField.setToolTipText("Tempo para quitar o financiamento em anos.");
        bottomLeftPanel.add(anosConclusaoField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelValorizado = new JLabel();
        valorImovelValorizado.setText("Valor Imóvel Valorização:");
        valorImovelValorizado.setToolTipText("Valor do imóvel considerando a valorização até o término do financiamento, caso tenha sido informada a taxa de expectativa de valorização nas opções avançadas.");
        bottomLeftPanel.add(valorImovelValorizado, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorImovelInflacao = new JLabel();
        valorImovelInflacao.setText("Valor Imóvel Inflação:");
        valorImovelInflacao.setToolTipText("Valor do imóvel considerando a inflação ao término do financiamento, caso tenha sido informada a taxa de expectativa de inflação nas opções avançadas.");
        bottomLeftPanel.add(valorImovelInflacao, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Salvar");
        saveButton.setToolTipText("Salva as simulações na mesma pasta em que está sendo executado o programa.");
        bottomLeftPanel.add(saveButton, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        loadButton = new JButton();
        loadButton.setText("Carregar");
        loadButton.setToolTipText("Carrega as simulações que estiverem salvas na mesma pasta em que está sendo executado o programa.");
        bottomLeftPanel.add(loadButton, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, -1), new Dimension(200, -1), null, 0, false));
        changeThemeButton = new JButton();
        Font changeThemeButtonFont = this.$$$getFont$$$(null, -1, -1, changeThemeButton.getFont());
        if (changeThemeButtonFont != null) changeThemeButton.setFont(changeThemeButtonFont);
        changeThemeButton.setText("Alterar Tema");
        changeThemeButton.setToolTipText("Altera entre tema claro e escuro");
        bottomLeftPanel.add(changeThemeButton, new GridConstraints(11, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoInvestido = new JLabel();
        saldoInvestido.setText("Saldo Valor Investido:");
        saldoInvestido.setToolTipText("Saldo do valor investido, caso tenha sido informado no campo Investimentos.");
        bottomLeftPanel.add(saldoInvestido, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valorizacaoReal = new JLabel();
        valorizacaoReal.setText("Valorização Real:");
        bottomLeftPanel.add(valorizacaoReal, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upperLeftPanel = new JPanel();
        upperLeftPanel.setLayout(new GridLayoutManager(25, 6, new Insets(20, 20, 20, 20), -1, -1));
        upperLeftPanel.setFocusable(false);
        upperLeftPanel.setForeground(new Color(-13947600));
        horizontalSplitPane.setLeftComponent(upperLeftPanel);
        upperLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        nomeFinanciamentoPanel = new JPanel();
        nomeFinanciamentoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        nomeFinanciamentoPanel.setToolTipText("Nome da simulação de financiamento");
        upperLeftPanel.add(nomeFinanciamentoPanel, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nomeFinanciamentoLabel = new JLabel();
        nomeFinanciamentoLabel.setText("Nome Financiamento");
        nomeFinanciamentoLabel.setToolTipText("Nome da simulação de financiamento");
        nomeFinanciamentoPanel.add(nomeFinanciamentoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        nomeFinanciamento = new JTextField();
        nomeFinanciamento.setToolTipText("Nome da simulação de financiamento");
        nomeFinanciamentoPanel.add(nomeFinanciamento, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorImovelPanel = new JPanel();
        valorImovelPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorImovelPanel.setToolTipText("Valor do Imóvel em reais");
        upperLeftPanel.add(valorImovelPanel, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorImovelLabel = new JLabel();
        valorImovelLabel.setText("Valor Imóvel (R$)");
        valorImovelLabel.setToolTipText("Valor do Imóvel em reais");
        valorImovelPanel.add(valorImovelLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorImovel = new JTextField();
        valorImovel.setToolTipText("Valor do Imóvel em reais");
        valorImovelPanel.add(valorImovel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualEntradaPanel = new JPanel();
        percentualEntradaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        percentualEntradaPanel.setToolTipText("Percentual de entrada");
        upperLeftPanel.add(percentualEntradaPanel, new GridConstraints(2, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentualEntradaLabel = new JLabel();
        percentualEntradaLabel.setText("Percentual Entrada (%)");
        percentualEntradaLabel.setToolTipText("Percentual de entrada");
        percentualEntradaPanel.add(percentualEntradaLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentualEntrada = new JSlider();
        percentualEntrada.setMajorTickSpacing(10);
        percentualEntrada.setMinorTickSpacing(10);
        percentualEntrada.setPaintLabels(true);
        percentualEntrada.setPaintTicks(false);
        percentualEntrada.setPaintTrack(true);
        percentualEntrada.setSnapToTicks(true);
        percentualEntrada.setToolTipText("Percentual de entrada");
        percentualEntrada.setValueIsAdjusting(false);
        percentualEntradaPanel.add(percentualEntrada, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jurosAnualPanel = new JPanel();
        jurosAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        jurosAnualPanel.setToolTipText("Júros Efetivo Anual");
        upperLeftPanel.add(jurosAnualPanel, new GridConstraints(4, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        jurosAnualLabel = new JLabel();
        jurosAnualLabel.setText("Juros Anual (%)");
        jurosAnualLabel.setToolTipText("Júros Efetivo Anual");
        jurosAnualPanel.add(jurosAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        jurosAnual = new JTextField();
        jurosAnual.setToolTipText("Júros Efetivo Anual");
        jurosAnualPanel.add(jurosAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraInicialPanel = new JPanel();
        valorExtraInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorExtraInicialPanel.setEnabled(true);
        valorExtraInicialPanel.setToolTipText("Valor que será amortizado na primeira recorrência");
        upperLeftPanel.add(valorExtraInicialPanel, new GridConstraints(7, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorExtraInicialLabel = new JLabel();
        valorExtraInicialLabel.setText("Valor Inicial (R$)");
        valorExtraInicialLabel.setToolTipText("Valor que será amortizado na primeira recorrência");
        valorExtraInicialPanel.add(valorExtraInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraInicial = new JTextField();
        valorExtraInicial.setText(" ");
        valorExtraInicial.setToolTipText("Valor que será amortizado na primeira recorrência");
        valorExtraInicialPanel.add(valorExtraInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        percentualProximoValorExtraPanel = new JPanel();
        percentualProximoValorExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        percentualProximoValorExtraPanel.setToolTipText("Percentual para amortizar a partir do valor anterior. Caso seja 100%, o valor será constante.");
        upperLeftPanel.add(percentualProximoValorExtraPanel, new GridConstraints(8, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        percentProximoValorExtraLabel = new JLabel();
        percentProximoValorExtraLabel.setText("Prox. Valor (%)");
        percentProximoValorExtraLabel.setToolTipText("Percentual para amortizar a partir do valor anterior. Caso seja 100%, o valor será constante.");
        percentProximoValorExtraLabel.putClientProperty("html.disable", Boolean.FALSE);
        percentualProximoValorExtraPanel.add(percentProximoValorExtraLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        percentProximoValorExtra = new JTextField();
        percentProximoValorExtra.setToolTipText("Percentual para amortizar a partir do valor anterior. Caso seja 100%, o valor será constante.");
        percentualProximoValorExtraPanel.add(percentProximoValorExtra, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorExtraMinimoPanel = new JPanel();
        valorExtraMinimoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorExtraMinimoPanel.setToolTipText("Valor mínimo para amortização");
        upperLeftPanel.add(valorExtraMinimoPanel, new GridConstraints(9, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, -1), null, 0, false));
        valorExtraMinimoLabel = new JLabel();
        valorExtraMinimoLabel.setText("Valor Mínimo (R$)");
        valorExtraMinimoLabel.setToolTipText("Valor mínimo para amortização");
        valorExtraMinimoPanel.add(valorExtraMinimoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorExtraMinimo = new JTextField();
        valorExtraMinimo.setToolTipText("Valor mínimo para amortização");
        valorExtraMinimoPanel.add(valorExtraMinimo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        valorInvestidoPanel = new JPanel();
        valorInvestidoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        valorInvestidoPanel.setToolTipText("Valor investido em reais");
        upperLeftPanel.add(valorInvestidoPanel, new GridConstraints(14, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        valorInvestidoLabel = new JLabel();
        valorInvestidoLabel.setText("Valor Investido (R$)");
        valorInvestidoLabel.setToolTipText("Valor investido em reais");
        valorInvestidoPanel.add(valorInvestidoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        valorInvestido = new JTextField();
        valorInvestido.setToolTipText("Valor investido em reais");
        valorInvestidoPanel.add(valorInvestido, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        rendimentoAnualPanel = new JPanel();
        rendimentoAnualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rendimentoAnualPanel.setToolTipText("Rendimento Anual bruto do produto de investimento contratado.");
        upperLeftPanel.add(rendimentoAnualPanel, new GridConstraints(15, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rendimentoAnualLabel = new JLabel();
        rendimentoAnualLabel.setText("Rendimento Anual (%)");
        rendimentoAnualLabel.setToolTipText("Rendimento Anual bruto do produto de investimento contratado.");
        rendimentoAnualPanel.add(rendimentoAnualLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        rendimentoAnual = new JTextField();
        rendimentoAnual.setToolTipText("Rendimento Anual bruto do produto de investimento contratado.");
        rendimentoAnualPanel.add(rendimentoAnual, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        salarioBrutoPanel = new JPanel();
        salarioBrutoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        salarioBrutoPanel.setToolTipText("Salário bruto em reais");
        upperLeftPanel.add(salarioBrutoPanel, new GridConstraints(19, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Salário Bruto (R$)");
        label1.setToolTipText("Salário bruto em reais");
        salarioBrutoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        salarioBruto = new JTextField();
        salarioBruto.setToolTipText("Salário bruto em reais");
        salarioBrutoPanel.add(salarioBruto, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        saldoAtualPanel = new JPanel();
        saldoAtualPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        saldoAtualPanel.setToolTipText("Saldo inicial no FGTS");
        upperLeftPanel.add(saldoAtualPanel, new GridConstraints(20, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Saldo FGTS (R$)");
        label2.setToolTipText("Saldo inicial no FGTS");
        saldoAtualPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        saldoFGTS = new JTextField();
        saldoFGTS.setToolTipText("Saldo inicial no FGTS");
        saldoAtualPanel.add(saldoFGTS, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        escolherSistemaPanel = new JPanel();
        escolherSistemaPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        escolherSistemaPanel.setToolTipText("Selecione o sistema de amortização");
        upperLeftPanel.add(escolherSistemaPanel, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Sistema Amortização");
        label3.setToolTipText("Selecione o sistema de amortização");
        escolherSistemaPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        sistemaAmortizacaoComboBox = new JComboBox();
        sistemaAmortizacaoComboBox.setToolTipText("Selecione o sistema de amortização");
        escolherSistemaPanel.add(sistemaAmortizacaoComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        prazoPanel = new JPanel();
        prazoPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        prazoPanel.setToolTipText("Duração do financiamento");
        upperLeftPanel.add(prazoPanel, new GridConstraints(5, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        prazoLabel = new JLabel();
        prazoLabel.setText("Prazo (meses)");
        prazoLabel.setToolTipText("Duração do financiamento");
        prazoPanel.add(prazoLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        prazo = new JTextField();
        prazo.setToolTipText("Duração do financiamento");
        prazoPanel.add(prazo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        mesInicialPanel = new JPanel();
        mesInicialPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mesInicialPanel.setToolTipText("Mês da primeira amortização");
        upperLeftPanel.add(mesInicialPanel, new GridConstraints(10, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mesInicialLabel = new JLabel();
        mesInicialLabel.setText("Mês Inicio");
        mesInicialLabel.setToolTipText("Mês da primeira amortização");
        mesInicialPanel.add(mesInicialLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        mesInicial = new JTextField();
        mesInicial.setToolTipText("Mês da primeira amortização");
        mesInicialPanel.add(mesInicial, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        intervaloPanel = new JPanel();
        intervaloPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        intervaloPanel.setToolTipText("Intervalo entre cada amortização (em meses)");
        upperLeftPanel.add(intervaloPanel, new GridConstraints(11, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intervaloLabel = new JLabel();
        intervaloLabel.setText("Intervalo");
        intervaloLabel.setToolTipText("Intervalo entre cada amortização (em meses)");
        intervaloPanel.add(intervaloLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(150, -1), null, 0, false));
        intervalo = new JTextField();
        intervalo.setToolTipText("Intervalo entre cada amortização (em meses)");
        intervaloPanel.add(intervalo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        quantidadePanel = new JPanel();
        quantidadePanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        quantidadePanel.setToolTipText("Quantidade de vezes que será amortizado.");
        upperLeftPanel.add(quantidadePanel, new GridConstraints(12, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        repeticoesLabel = new JLabel();
        repeticoesLabel.setText("Repetições");
        repeticoesLabel.setToolTipText("Quantidade de vezes que será amortizado.");
        quantidadePanel.add(repeticoesLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        repeticoes = new JTextField();
        repeticoes.setToolTipText("Quantidade de vezes que será amortizado.");
        quantidadePanel.add(repeticoes, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        sempreCheckBox = new JCheckBox();
        sempreCheckBox.setEnabled(true);
        sempreCheckBox.setSelected(true);
        sempreCheckBox.setText("Sempre");
        sempreCheckBox.setToolTipText("Caso selecionado, as amortizações serão recorrentes até o final do financiamento");
        quantidadePanel.add(sempreCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        amortizacaoExtraPanel = new JPanel();
        amortizacaoExtraPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        amortizacaoExtraPanel.setToolTipText("Sessão destinada a considerar amortizações recorrentes, conforme valores, intervalo e repetições desejadas");
        upperLeftPanel.add(amortizacaoExtraPanel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        recorrencia = new JLabel();
        Font recorrenciaFont = this.$$$getFont$$$(null, Font.BOLD, 14, recorrencia.getFont());
        if (recorrenciaFont != null) recorrencia.setFont(recorrenciaFont);
        recorrencia.setText("Recorrência");
        recorrencia.setToolTipText("Sessão destinada a considerar amortizações recorrentes, conforme valores, intervalo e repetições desejadas");
        amortizacaoExtraPanel.add(recorrencia, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        recorrenciaCheckBox = new JCheckBox();
        Font recorrenciaCheckBoxFont = this.$$$getFont$$$(null, Font.BOLD, 14, recorrenciaCheckBox.getFont());
        if (recorrenciaCheckBoxFont != null) recorrenciaCheckBox.setFont(recorrenciaCheckBoxFont);
        recorrenciaCheckBox.setText("");
        recorrenciaCheckBox.setToolTipText("Sessão destinada a considerar amortizações recorrentes, conforme valores, intervalo e repetições desejadas");
        amortizacaoExtraPanel.add(recorrenciaCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        investimentosPanel = new JPanel();
        investimentosPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        investimentosPanel.setToolTipText("Sessão destinada a considerar amortizações a partir dos lucros de um investimento, ou simular o valor total investido ao término do financiamento. O IR considerado é de 15%, alíquota vigente para aplicações financeiras de no mínimo 2 anos.");
        upperLeftPanel.add(investimentosPanel, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        investimentosLabel = new JLabel();
        Font investimentosLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, investimentosLabel.getFont());
        if (investimentosLabelFont != null) investimentosLabel.setFont(investimentosLabelFont);
        investimentosLabel.setText("Investimentos");
        investimentosLabel.setToolTipText("Sessão destinada a considerar amortizações a partir dos lucros de um investimento, ou simular o valor total investido ao término do financiamento. O IR considerado é de 15%, alíquota vigente para aplicações financeiras de no mínimo 2 anos.");
        investimentosPanel.add(investimentosLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        investimentosCheckBox = new JCheckBox();
        investimentosCheckBox.setText("");
        investimentosCheckBox.setToolTipText("Sessão destinada a considerar amortizações a partir dos lucros de um investimento, ou simular o valor total investido ao término do financiamento. O IR considerado é de 15%, alíquota vigente para aplicações financeiras de no mínimo 2 anos.");
        investimentosPanel.add(investimentosCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fgtsPanel = new JPanel();
        fgtsPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        fgtsPanel.setToolTipText("Sessão destinada a considerar amortizações a partir do FGTS. O intervalo considerado para amortizar é a cada 2 anos, com rendimento de 3% ao ano, e com aportes iguais a 8% do salário bruto mensalmente.");
        upperLeftPanel.add(fgtsPanel, new GridConstraints(18, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fgtsLabel = new JLabel();
        fgtsLabel.setEnabled(true);
        Font fgtsLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, fgtsLabel.getFont());
        if (fgtsLabelFont != null) fgtsLabel.setFont(fgtsLabelFont);
        fgtsLabel.setText("FGTS");
        fgtsLabel.setToolTipText("Sessão destinada a considerar amortizações a partir do FGTS. O intervalo considerado para amortizar é a cada 2 anos, com rendimento de 3% ao ano, e com aportes iguais a 8% do salário bruto mensalmente.");
        fgtsPanel.add(fgtsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        fgtsCheckBox = new JCheckBox();
        fgtsCheckBox.setText("");
        fgtsCheckBox.setToolTipText("Sessão destinada a considerar amortizações a partir do FGTS. O intervalo considerado para amortizar é a cada 2 anos, com rendimento de 3% ao ano, e com aportes iguais a 8% do salário bruto mensalmente.");
        fgtsPanel.add(fgtsCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        advancedOptionsButton = new JButton();
        advancedOptionsButton.setText("Opções Avançadas");
        advancedOptionsButton.setToolTipText("Permite informar outros parâmetros opcionais.");
        upperLeftPanel.add(advancedOptionsButton, new GridConstraints(23, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        upperLeftPanel.add(spacer2, new GridConstraints(21, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 20), null, 0, false));
        calcularButton = new JButton();
        calcularButton.setEnabled(true);
        Font calcularButtonFont = this.$$$getFont$$$(null, -1, -1, calcularButton.getFont());
        if (calcularButtonFont != null) calcularButton.setFont(calcularButtonFont);
        calcularButton.setHideActionText(false);
        calcularButton.setHorizontalTextPosition(0);
        calcularButton.setText("Calcular");
        calcularButton.setToolTipText("Calcula a simulação com os dados informados.");
        upperLeftPanel.add(calcularButton, new GridConstraints(24, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resgatePanel = new JPanel();
        resgatePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        resgatePanel.setToolTipText("Tempo até o resgate em anos");
        upperLeftPanel.add(resgatePanel, new GridConstraints(16, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        resgateLabel = new JLabel();
        resgateLabel.setText("Resgate em (anos)");
        resgateLabel.setToolTipText("Tempo até o resgate em anos");
        resgatePanel.add(resgateLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resgate = new JTextField();
        resgate.setToolTipText("Tempo até o resgate em anos");
        resgatePanel.add(resgate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        acaoResgatePanel = new JPanel();
        acaoResgatePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        acaoResgatePanel.setToolTipText("Ação a ser tomada no momento do resgate");
        upperLeftPanel.add(acaoResgatePanel, new GridConstraints(17, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        acaoNoResgate = new JLabel();
        acaoNoResgate.setText("Ação");
        acaoNoResgate.setToolTipText("Ação a ser tomada no momento do resgate");
        acaoResgatePanel.add(acaoNoResgate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, -1), new Dimension(100, -1), null, 0, false));
        resgateComboBox = new JComboBox();
        resgateComboBox.setToolTipText("Ação a ser tomada no momento do resgate");
        acaoResgatePanel.add(resgateComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        resetarCamposButton = new JButton();
        resetarCamposButton.setText("Resetar Campos");
        upperLeftPanel.add(resetarCamposButton, new GridConstraints(22, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        verticalSplitPlane.setRightComponent(rightPanel);
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

    private void createUIComponents() {
        tabs = new JTabbedPaneCloseButton(simulationsList);
    }
}
