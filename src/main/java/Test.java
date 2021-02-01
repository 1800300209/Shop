import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class Test {
    static int count=0;
    static Product[] carts = new Product[10];
    static Order[] orders = new Order[10];
    static String[] date=new String[10];
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static String usern=null;
    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException {
        int flag = 0;
        Scanner sc = new Scanner(System.in);
        while(flag==0) {
            System.out.println("请输入用户名：");
            String username = sc.next();
            System.out.println("您输入的用户名为：" + username);

            System.out.println("请输入密码：");
            String password = sc.next();
            System.out.println("您输入的密码为：" + password);

            File f = new File("D:\\Shop\\src\\Users.xlsx");
            InputStream in=Class.forName("Test").getResourceAsStream("Users.xlsx");
            ReadUserExcel readUser = new ReadUserExcel();
            User user[] = readUser.readExcel(in);

            for (int i = 0; i < user.length; i++) {
                if (username.equals(user[i].getUsername()) && password.equals(user[i].getPassword())) {
                    usern=username;
                    flag++;
                }
            }
            if (flag > 0) {
                System.out.println("登陆成功！");
                flag=0;break;
            } else
                System.out.println("登陆失败！");
        }
        ReadProductExcel r=new ReadProductExcel();
        r.read();
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
                    sum(carts);
                    Order[] o=toorder(carts);
                    createXL(o);
                }
                else {
                    System.out.println("您的购物车内无商品!");
                }
            }
            if(choose==4){
                System.out.println("退出成功！");
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
        System.out.println("请输入购买该商品的数量：");
        int n=sc.nextInt();
        Product product=r.getProductById(x,inProduct,n);
        if(product.getId()!=null){
            System.out.println("要购买的商品的总价为："+product.getPrice()*n);
            int flag=0;
            if(count>0){    //购物车内有商品
                for(int i=0;i<count;i++){       //判断该商品是否已在购物车内
                    if(carts[i].getId().equals(product.getId())){       //该商品在购物车内，数量加一
                        date[i]=df.format(new Date());
                        carts[i].setNumber(carts[i].getNumber()+n);
                        flag++;
                    }
                }
                if(flag==0){        //该商品不在购物车内
                    date[count]=df.format(new Date());
                    carts[count++]=product;
                }
            }
            else {   //购物车内无商品
                date[count]=df.format(new Date());
                carts[count++]=product;
            }
        }
        else{
            System.out.println("该商品不存在！");
        }
    }
    public static void createXL(Order[] o){
        /** Excel 文件要存放的位置，假定在D盘下*/
        String outputFile = "D:\\Shop\\src\\carts.xlsx";
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("订单");
            String[] str={"用户","商品","购买数量","商品总价","实付款","下单时间"};
            int length=0;
            for(int i=0;i<o.length;i++){
                if(o[i]!=null){
                    length++;
                }
            }
            // 在索引0的位置创建行（最顶端的行）
            HSSFRow[] row =new HSSFRow[length+1];
            //在索引0的位置创建单元格（左上端）
            HSSFCell[][] cell =new HSSFCell[length+1][6];
            row[0] = sheet.createRow((short)0);
            for(int j=0;j<6;j++){
                cell[0][j] = row[0].createCell((short)j);
                cell[0][j].setCellValue(str[j]);
            }
            for(int i=1;i<length+1;i++){
                row[i] = sheet.createRow((short)i);
                for(int j=0;j<6;j++){
                    cell[i][j] = row[i].createCell((short)j);
                }
                cell[i][0].setCellValue(o[i-1].getUsername());
                cell[i][1].setCellValue(o[i-1].getName());
                cell[i][2].setCellValue(o[i-1].getNumber());
                cell[i][3].setCellValue(o[i-1].getTotal_price());
                cell[i][4].setCellValue(o[i-1].getUntil_price());
                cell[i][5].setCellValue(o[i-1].getTime());
            }
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
    public static Order[] toorder(Product[] p){
        int length=0;
        for(int i=0;i<p.length;i++){
            if(p[i]!=null){
                length++;
            }
        }
        Order[] o=new Order[length];
        for(int j=0;j<length;j++){
            o[j]=new Order();
            o[j].setUsername(usern);
            o[j].setName(p[j].getId());
            o[j].setNumber(p[j].getNumber());
            o[j].setTotal_price(p[j].getNumber()*p[j].getPrice());
            o[j].setUntil_price(p[j].getNumber()*p[j].getPrice());
            o[j].setTime(date[j]);
        }
        return o;
    }
}
