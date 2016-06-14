package history.write;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Vacilo on 22/04/2016.
 */
public class WriteXLS_NeuroSky {

    public static void writeXLS(String name,Object [] [] bookData,int rows) {

        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("NeuroSky");
            File file=new File(name+".xlsx");
            if(!file.exists()){
            	Row row_ = sheet.createRow(0);
				System.out.println("1ªtime");
                for(int i = 0; i < 12; i++ ) {
                    Cell cell = row_.createCell(i);
                    switch(i) {
                        case 0:
                            cell.setCellValue("Time");
                            break;
                        case 1:
                            cell.setCellValue("Delta");
                            break;
                        case 2:
                            cell.setCellValue("Theta");
                            break;
                        case 3:
                            cell.setCellValue("Low Alpha");
                            break;
                        case 4:
                            cell.setCellValue("High Alpha");
                            break;
                        case 5:
                            cell.setCellValue("Low Beta");
                            break;
                        case 6:
                            cell.setCellValue("High Beta");
                            break;
                        case 7:
                            cell.setCellValue("Low Gamma");
                            break;
                        case 8:
                            cell.setCellValue("High Gamma");
                            break;
                        case 9:
                            cell.setCellValue("Attention");
                            break;
                        case 10:
                            cell.setCellValue("Meditation");
                            break;
                        case 11:
                            cell.setCellValue("Signal");
                            break;
                    }
                }
            }            

            for (Object[] aBook : bookData) {
                Row row = sheet.createRow(++rows);

                int cols = -1; // Nr de colunas

                for (Object field : aBook) {
                    Cell cell = row.createCell(++cols);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Double) {
                        cell.setCellValue((Double) field);
                    }
                }

            }
            System.err.println(rows);
            name+= ".xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(name,true)) {
                wb.write(outputStream);
                outputStream.close();
            }


        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

}