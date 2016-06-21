package history.read.net.codejava.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadXLS {

    public static String[][] read(String fileName) {
        XSSFRow row;
        XSSFCell cell;
        String[][] value = null;

        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // get sheet number
            int sheetCn = workbook.getNumberOfSheets();

            for (int cn = 0; cn < sheetCn; cn++) {

                // get 0th sheet data
                XSSFSheet sheet = workbook.getSheetAt(cn);

                // get number of rows from sheet
                int rows = sheet.getPhysicalNumberOfRows();

                // get number of cell from row
                int cells = sheet.getRow(cn).getPhysicalNumberOfCells();

                value = new String[rows][cells];

                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r); // bring row
                    if (row != null) {
                        for (int c = 0; c < cells; c++) {
                            cell = row.getCell(c);

                            if (cell != null) {

                                switch (cell.getCellType()) {

                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        value[r][c] = cell.getCellFormula();
                                        break;

                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        value[r][c] = ""
                                                + cell.getNumericCellValue();
                                        break;

                                    case XSSFCell.CELL_TYPE_STRING:
                                        value[r][c] = ""
                                                + cell.getStringCellValue();
                                        break;

                                    case XSSFCell.CELL_TYPE_BLANK:
                                        value[r][c] = "[BLANK]";
                                        break;

                                    case XSSFCell.CELL_TYPE_ERROR:
                                        value[r][c] = ""+cell.getErrorCellValue();
                                        break;
                                    default:
                                }
                               // System.out.print("  "+value[r][c]);

                            } else {
                                //System.out.print("[Null]\t");
                            }
                        } // for(c)
                        //System.out.print("\n");
                    }
                } // for(r)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}