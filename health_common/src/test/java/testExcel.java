import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author hua
 * @date 2019/07/23 13:19
 **/
public class testExcel {

    @Test
    public void test01() throws IOException {
        //创建工作蒲对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\SomeThingForIdea\\word.xlsx");
        //获得工作表对象,一个excel表中有三个,选中第一个
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历工作表 获得行对象
        for (Row row : sheet) {
            //遍历行对象,获得列对象
            for (Cell cell : row) {
                //获得列里面的内容
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("----------------");
        }
        //关闭
        workbook.close();
    }

    @Test
    public void test02() throws IOException {
        //创建工作蒲对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\SomeThingForIdea\\word.xlsx");
        //获得工作表对象,一个excel表中有三个,选中第一个
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0;j < lastCellNum;j++){
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
    }

    @Test
    public void test03() throws IOException {
        //创建工作蒲对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(1);
        cell.setCellValue("你好");
        XSSFCell cell2 = row.createCell(2);
        cell2.setCellValue("你好2");
        XSSFCell cell3 = row.createCell(3);
        cell3.setCellValue("你好3");
        //通过输出流输出到本地硬盘
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\SomeThingForIdea\\test.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();
    }

    @Test
    public void test04(){
//        System.out.println(new Date().getDate());
        Date date = new Date();
        System.out.println(date.getMonth());
    }

    @Test
    public void test05(){
        String varilable = "spring_xml";
        String[] split = varilable.split("_");
        Arrays.stream(split).forEach(str ->{
            System.out.println(str);
        });
    }

    @Test
    public void test06(){
        int i = 2;
        for (i = 2; i < 100; i++) {
            if (isPrime(i)) {
                System.out.println(i);
            }
        }
    }

    public boolean isPrime(int num){
        for (int i = 2; i < num; i++) {
            if (num%i == 0){
                return false;
            }
        }
        return true;
    }
}
