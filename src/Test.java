import java.io.File;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;

public class Test {
    static int count=0;
    static Product[] carts = new Product[10];
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException {
        int flag = 0;
        Scanner sc = new Scanner(System.in);
//        while(flag==0) {
//            System.out.println("请输入用户名：");
//            String username = sc.next();
//            System.out.println("您输入的用户名为：" + username);
//
//            System.out.println("请输入密码：");
//            String password = sc.next();
//            System.out.println("您输入的密码为：" + password);
//
//            File f = new File("D:\\Shop\\src\\Users.xlsx");
//            InputStream in=Class.forName("Test").getResourceAsStream("Users.xlsx");
//            ReadUserExcel readUser = new ReadUserExcel();
//            User user[] = readUser.readExcel(in);
//
//            for (int i = 0; i < user.length; i++) {
//                if (username.equals(user[i].getUsername()) && password.equals(user[i].getPassword())) {
//                    flag++;
//                }
//            }
//            if (flag > 0) {
//                System.out.println("登陆成功！");
//                flag=0;break;
//            } else
//                System.out.println("登陆失败！");
//        }
        int choose=0;
        while(true){
            System.out.println("继续购物请按1");
            System.out.println("查看购物车请按2");
            System.out.println("结账请按3");
            System.out.println("退出请按4");
            choose=sc.nextInt();
            if(choose==1){
                shop();
            }
            if(choose==2){
                if(carts[0]!=null){
                    for(int i=0;carts[i]!=null;i++){
                        System.out.println("id:"+carts[i].getId()+"    name:"+carts[i].getName()+"    price:"+carts[i].getPrice()+"    number:"+carts[i].getNumber()+"    desc:"+carts[i].getDesc());
                    }
               }
                else {
                    System.out.println("您的购物车内无商品!");
                }
            }
            if(choose==3){
                if(carts[0]!=null){
                    double d=sum(carts);
                    System.out.println("您的订单总价为："+d);
                    createXL(carts);
                }
                else {
                    System.out.println("您的购物车内无商品!");
                }
            }
            if(choose==4){
                break;
            }
        }
    }
    public static void shop() throws ClassNotFoundException, IllegalAccessException {
        ReadProductExcel r=new ReadProductExcel();
        r.read();
        InputStream inProduct=null;
        Scanner sc = new Scanner(System.in);
        inProduct=Class.forName("Test").getResourceAsStream("Products.xlsx");
        System.out.println("请输入物品id将其加入购物车：");
        String x = sc.next();
        Product product=r.getProductById(x,inProduct);
        if(product.getId()!=null){
            System.out.println("要购买的商品的价格："+product.getPrice());
            int flag=0;
            if(count>0){
                    for(int i=0;i<count;i++){
                        if(carts[i].getId().equals(product.getId())){
                            carts[i].setNumber(carts[i].getNumber()+1);
                            flag++;
                        }
                    }
                    if(flag==0){
                        carts[count++]=product;
                    }
                }
            else {
                carts[count++]=product;
            }
        }
        else{
            System.out.println("该商品不存在！");
        }
    }
    public static void createXL(Product[] p){
        /** Excel 文件要存放的位置，假定在D盘下*/
        String outputFile = "D:\\Shop\\src\\carts.xlsx";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("订单");
            String[] s={"id","name","price","number","desc"};
            int length=0;
            for(int i=0;i<p.length;i++){
                if(p[i]!=null){
                    length++;
                }
            }
            // 在索引0的位置创建行（最顶端的行）
            HSSFRow[] row =new HSSFRow[length+2];
            System.out.println(row.length);
            //在索引0的位置创建单元格（左上端）
            HSSFCell[][] cell =new HSSFCell[length+2][5];
            row[0] = sheet.createRow((short)0);
            for(int j=0;j<5;j++){
                cell[0][j] = row[0].createCell((short)j);
                cell[0][j].setCellValue(s[j]);
            }
            for(int i=1;i<length+1;i++){
                row[i] = sheet.createRow((short)i);
                for(int j=0;j<5;j++){
                    cell[i][j] = row[i].createCell((short)j);
                }
                cell[i][0].setCellValue(p[i-1].getId());
                cell[i][1].setCellValue(p[i-1].getName());
                cell[i][2].setCellValue(p[i-1].getPrice());
                cell[i][3].setCellValue(p[i-1].getNumber());
                cell[i][4].setCellValue(p[i-1].getDesc());
            }
            row[length+1] = sheet.createRow((short)length+1);
            cell[length+1][0] = row[length+1].createCell((short)0);
            cell[length+1][0].setCellValue("订单总价为："+sum(p));
            // 在单元格中输入一些内容
            // 新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(outputFile);
            // 把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            System.out.println("订单生成成功！");
        } catch (Exception e) {
            System.out.println("已运行 xlCreate() : " + e);
        }
    }
    public static double sum(Product[] p){
        double d = 0;
        if(carts[0]!=null) {
            for (int i = 0; carts[i] != null; i++) {
                System.out.println("id:" + carts[i].getId() + "    name:" + carts[i].getName() + "    price:" + carts[i].getPrice() + "    number:" + carts[i].getNumber() + "    desc:" + carts[i].getDesc());
            }
            for (int i = 0; carts[i] != null; i++) {
                d += carts[i].getPrice() * carts[i].getNumber();
            }
        }
        return d;
    }
}
