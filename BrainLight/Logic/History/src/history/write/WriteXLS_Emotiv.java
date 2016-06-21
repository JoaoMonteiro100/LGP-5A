package history.write;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class WriteXLS_Emotiv {
    private static XSSFWorkbook wb;
    private static String fileName;

    public static void writeXLS(String name, Object [] [] bookData) {
        try {
            File fileFolder= new File("history");

            Sheet sheet;
            if(!fileFolder.exists()){
                fileFolder.mkdir();
            }
            File file=new File("history/"+name+".xlsx");
            if(!file.exists()) {
                wb = new XSSFWorkbook();
                fileName = name;
                sheet = wb.createSheet(file.getName());
                Row row_ = sheet.createRow(0);
                for (int i = 0; i < 20; i++) {
                    Cell cell = row_.createCell(i);
                    switch (i) {
                        case 0:
                            cell.setCellValue("Time");
                            break;
                        case 1:
                            cell.setCellValue("Alpha");
                            break;
                        case 2:
                            cell.setCellValue("Beta");
                            break;
                        case 3:
                            cell.setCellValue("Delta");
                            break;
                        case 4:
                            cell.setCellValue("Teta");
                            break;
                        case 5:
                            cell.setCellValue("Blink");
                            break;
                        case 6:
                            cell.setCellValue("Left Wink");
                            break;
                        case 7:
                            cell.setCellValue("Right Wink");
                            break;
                        case 8:
                            cell.setCellValue("Eyes Open");
                            break;
                        case 9:
                            cell.setCellValue("Smile Extension");
                            break;
                        case 10:
                            cell.setCellValue("Clench Extension");
                            break;
                        case 11:
                            cell.setCellValue("Looking Down");
                            break;
                        case 12:
                            cell.setCellValue("Looking Up");
                            break;
                        case 13:
                            cell.setCellValue("Looking Left");
                            break;
                        case 14:
                            cell.setCellValue("Looking Right");
                            break;
                        case 15:
                            cell.setCellValue("Engagement");
                            break;
                        case 16:
                            cell.setCellValue("Excitement Long Time");
                            break;
                        case 17:
                            cell.setCellValue("Excitement Short Time");
                            break;
                        case 18:
                            cell.setCellValue("Frustration");
                            break;
                        case 19:
                            cell.setCellValue("Meditation");
                            break;
                    }
                }
            }else{
                if(!fileName.equals(name)){
                    wb=new XSSFWorkbook();
                    fileName=name;
                }
                if(wb.getNumberOfSheets()!=0)
                    sheet = wb.getSheetAt(0);
                else{
                    sheet = wb.createSheet(file.getName());
                    Row row_ = sheet.createRow(0);
                }
            }
            for (Object[] aBook : bookData) {
                Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());

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
            name+= ".xlsx";
            try (FileOutputStream outputStream = new FileOutputStream("history/"+name)) {
                wb.write(outputStream);
                outputStream.close();
                System.out.println("olasdasdlpqwekopq");
            }


        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

}