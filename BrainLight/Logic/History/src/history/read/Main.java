package history.read;

import history.read.net.codejava.excel.ReadXLS;

public class Main {

    public static void main(String[] args) {

        ReadXLS xlsRead = null;
        String[][] excel = xlsRead.read("history/Qua_2016_06_15_at_04_11_17.xlsx"); //cenas = name of excel file

        //tests
        for(int i = 0; i < excel.length; i++){
            for(int k = 0; k < excel[i].length; k++) {
                System.out.print(" " + excel[i][k]);
            }
            System.out.println("\n");
        }
    }
}
