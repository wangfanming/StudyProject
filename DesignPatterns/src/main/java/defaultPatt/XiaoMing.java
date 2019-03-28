package defaultPatt;

/**
 * 适配器模式的用意是要改变源的接口，以便于目标接口相容。缺省适配的用意稍有不同，
 * 它是为了方便建立一个不平庸的适配器类而提供的一种平庸实现。
 */
public class XiaoMing extends NormalStudent {
    @Override
    public void dance(){
        System.out.println("小明会跳舞");
    }

    @Override
    public void study() {
        System.out.println("小明爱学习");
    }
}
