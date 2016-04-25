import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * Created by Vacilo on 22/04/2016.
 */
public class WriteXLS_Emotiv {

    public static void writeXLS(String name, Object [] [] bookData) {


        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("EMOTIV");

            int rows = 0; // Nr de linhas

            Row row_ = sheet.createRow(0);

            for(int i = 0; i < 21; i++ ) {
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
                        cell.setCellValue("Alpha");
                        break;
                    case 4:
                        cell.setCellValue("Low Beta");
                        break;
                    case 5:
                        cell.setCellValue("High Beta");
                        break;
                    case 6:
                        cell.setCellValue("Gama");
                        break;
                    case 7:
                        cell.setCellValue("Teta");
                        break;
                    case 8:
                        cell.setCellValue("Blink");
                        break;
                    case 9:
                        cell.setCellValue("Left Wink");
                        break;
                    case 10:
                        cell.setCellValue("Right Wink");
                        break;
                    case 11:
                        cell.setCellValue("Eyes Open");
                        break;
                    case 12:
                        cell.setCellValue("Smile Extension");
                        break;
                    case 13:
                        cell.setCellValue("Clench Extension");
                        break;
                    case 14:
                        cell.setCellValue("Looking Down");
                        break;
                    case 15:
                        cell.setCellValue("Looking Up");
                        break;
                    case 16:
                        cell.setCellValue("Looking Left");
                        break;
                    case 17:
                        cell.setCellValue("Looking Right");
                        break;
                    case 18:
                        cell.setCellValue("Push");
                        break;
                    case 19:
                        cell.setCellValue("Pull");
                        break;
                    case 20:
                        cell.setCellValue("Lift");
                        break;
                    case 21:
                        cell.setCellValue("Drop");
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
