import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * Created by Vacilo on 22/04/2016.
 */
public class WriteXLS_NeuroSky {

    public static void writeXLS(String name, Object [] [] bookData) {

        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("NeuroSky");

            int rows = 0; // Nr de linhas

            Row row_ = sheet.createRow(0);

            for(int i = 0; i < 12; i++ ) {
                Cell cell = row_.createCell(i);

                switch(i) {
                    case 0:
                        cell.setCellValue("Time");
                        break;
                    case 1:
                        cell.setCellValue("Attention");
                        break;
                    case 2:
                        cell.setCellValue("Meditation");
                        break;
                    case 3:
                        cell.setCellValue("Delta");
                        break;
                    case 4:
                        cell.setCellValue("Low Beta");
                        break;
                    case 5:
                        cell.setCellValue("High Beta");
                        break;
                    case 6:
                        cell.setCellValue("Low Alpha");
                        break;
                    case 7:
                        cell.setCellValue("High Alpha");
                        break;
                    case 8:
                        cell.setCellValue("Low Gamma");
                        break;
                    case 9:
                        cell.setCellValue("High Gamma");
                        break;
                    case 10:
                        cell.setCellValue("Theta");
                        break;
                    case 11:
                        cell.setCellValue("Error Rate");
                        break;
                }
            }

            for (Object[] aBook : bookData) {
                Row row = sheet.createRow(++rows);

                int cols = -1; // Nr de colunas

                for (Object field : aBook) {
                    Cell cell = row.createCell(++cols);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }

            }
            name+= ".xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(name)) {
                wb.write(outputStream);
            }


        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

}

