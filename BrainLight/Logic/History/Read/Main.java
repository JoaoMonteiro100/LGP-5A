import net.codejava.excel.XLSREAD;

public class Main {

    public static void main(String[] args) {

        XLSREAD xlsRead = null;
        String[][] excel = xlsRead.read("cenas"); //cenas = name of excel file

        //tests
        for(int i = 0; i < excel.length; i++){
            for(int k = 0; k < excel[i].length; k++) {
                System.out.print(" " + excel[i][k]);
            }
            System.out.println("\n");
        }
    }
}
