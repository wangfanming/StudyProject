import com.sun.jna.Native;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName JnaTest
 * @Descripyion TODO
 * @date 2020/6/12 22:36
 */
public class JnaTest {
    public static int max(int a, int b) {
        String path = JnaTest.class.getClassLoader().getResource("").getPath();
        System.out.println(path);
        HelloIntetface hello = (HelloIntetface) Native.loadLibrary(path + "hello.dll", HelloIntetface.class);
        return hello.max(a, b);
    }

    public static void main(String[] args) {
        System.out.println(max(2, 6));
    }
}
