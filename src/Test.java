import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.next();
        System.out.println("您输入的用户名为：" + username);

        System.out.println("请输入密码：");
        String password = sc.next();
        System.out.println("您输入的密码为：" + password);

        File f = new File("D:\\Shop\\src\\massage.xlsx");
        ReadExcel read = new ReadExcel();
        User user[] = read.readExcel(f);
        int flag = 0;
        for (int i = 0; i < user.length; i++) {
            if (username.equals(user[i].getUsername()) && password.equals(user[i].getPassword())) {
                flag++;
            }
        }
        if (flag > 0) {
            System.out.println("登陆成功！");
        } else
            System.out.println("登陆失败！");
    }
}
