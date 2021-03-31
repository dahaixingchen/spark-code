/**
 * Description:
 *
 * @ClassName: Test1
 * @Author chengfei
 * @Date 2021/3/31 13:43
 **/
public class Test1 {
    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;

        System.out.println(a&&b||a);//true
        System.out.println(b||a&&b||b);//true
        System.out.println(b||a&&b||a);//true
    }
}
