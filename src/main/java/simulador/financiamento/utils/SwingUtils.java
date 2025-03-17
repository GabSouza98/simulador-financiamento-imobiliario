package simulador.financiamento.utils;

import simulador.financiamento.sistemas.amortizacao.SistemaAmortizacao;
import simulador.financiamento.tabela.TableRow;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SwingUtils {

    public static JTable getSimulationJTable(SistemaAmortizacao sistemaAmortizacao) {
        // Column Names
        final String[] columnNames = Constants.SIMULATION_COLUMN_NAMES;
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

}
