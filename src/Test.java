import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String args[]) {
        int flag = 0;
        while(flag==0) {

            Scanner sc = new Scanner(System.in);
            System.out.println("请输入用户名：");
            String username = sc.next();
            System.out.println("您输入的用户名为：" + username);

            System.out.println("请输入密码：");
            String password = sc.next();
            System.out.println("您输入的密码为：" + password);

            File f = new File("src\\massage.xlsx");
            ReadExcel read = new ReadExcel();
            User user[] = read.readExcel(f);

            for (int i = 0; i < user.length; i++) {
                if (username.equals(user[i].getUsername()) && password.equals(user[i].getPassword())) {
                    flag++;
                }
            }
            if (flag > 0) {
                System.out.println("登陆成功！");
                flag=0;break;
            } else
                System.out.println("登陆失败！");
        }
    }
}
