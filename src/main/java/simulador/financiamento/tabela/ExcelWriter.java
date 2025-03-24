package simulador.financiamento.tabela;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import simulador.financiamento.dominio.sistemas.SistemaAmortizacao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    public void writeTable(SistemaAmortizacao sistemaAmortizacao) {

        List<String> tabela = sistemaAmortizacao.getTabela();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(sistemaAmortizacao.getNomeFinanciamento());

        //Set column width for each column and populates header
        String[] headerData = tabela.get(0).split(",");
        Row header = sheet.createRow(0);
        for (int z=0; z<headerData.length; z++) {
            sheet.setColumnWidth(z, 6000);
            Cell cell = header.createCell(z);
            cell.setCellValue(headerData[z]);
            cell.setCellStyle(getHeaderStyle(workbook));
        }

        //Create row for each line and set each cell value in the row
        for (int i=1; i<tabela.size(); i++) {
            Row row = sheet.createRow(i);
            String[] data = tabela.get(i).split(",");
            for (int j=0; j<data.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(data[j]);
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(sistemaAmortizacao.getNomeFinanciamento() + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            System.out.println("Error in writing file: " + e);
        }
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.NO_FILL);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }
}
