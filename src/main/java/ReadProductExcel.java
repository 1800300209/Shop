import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class ReadProductExcel {
    public  void read() throws ClassNotFoundException, IllegalAccessException {
        InputStream in=Class.forName("Test").getResourceAsStream("/Products.xlsx");
        ReadProductExcel read = new ReadProductExcel();
        Product products[] = read.readExcel(in);
        Class productClass=Product.class;
        Field[] fields=productClass.getDeclaredFields();
        for(int j=0;j<products.length;j++){
//            for(Field field:fields){
//                field.setAccessible(true);
//                System.out.println(field.getName()+": "+field.get(products[j]));
//            }
//            System.out.println("");
            System.out.println("id:"+products[j].getId()+"    name:"+products[j].getName()+"    price:"+products[j].getPrice()+"    desc:"+products[j].getDesc());
        }
    }
    public Product[] readExcel(InputStream in) {
        Product products[] = null;
        try {
            XSSFWorkbook xw = new XSSFWorkbook(in);
            XSSFSheet xs = xw.getSheetAt(0);
            products = new Product[xs.getLastRowNum()];
            for (int j = 1; j <= xs.getLastRowNum(); j++) {
                XSSFRow row = xs.getRow(j);
                Product product = new Product();//每循环一次就把电子表格的一行的数据给对象赋值
                for (int k = 0; k <= row.getLastCellNum(); k++) {
                    XSSFCell cell = row.getCell(k);
                    if (cell == null)
                        continue;
                    if (k == 0) {
                        product.setId(this.getValue(cell));
                    } else if (k == 1) {
                        product.setName(this.getValue(cell));
                    } else if (k == 2) {
                        product.setPrice(Float.parseFloat(this.getValue(cell)));
                    } else if (k == 3) {
                        product.setNumber(Integer.parseInt(this.getValue(cell)));
                    }else if (k == 4) {
                        product.setDesc(this.getValue(cell));
                    }
                }
                products[j-1] = product;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    private String getValue(XSSFCell cell) {
        String value;
        CellType type = cell.getCellType();

        switch (type) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BLANK:
                value = "";
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            case NUMERIC:
                DecimalFormat df=new DecimalFormat("#");
                value = df.format(cell.getNumericCellValue());
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            case ERROR:
                value = "非法字符";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }
    public Product getProductById(String i,InputStream in){
        ReadProductExcel read = new ReadProductExcel();
        Product products[] = read.readExcel(in);
        Product product =new Product();
        for(int j=0;j<products.length;j++){
            if(products[j].getId().equals(i)){
                product.setId(products[j].getId());
                product.setName(products[j].getName());
                product.setNumber(1);
                product.setPrice(products[j].getPrice());
                product.setDesc(products[j].getDesc());
            }
        }
        return product;
    }
}