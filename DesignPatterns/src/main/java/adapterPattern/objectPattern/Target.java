package adapterPattern.objectPattern;

/**
 * Target：所期待的接口
 */
public interface Target {

    /**
     * 类Adaptee也有的方法
     */
    public void samplieOperation1();

    /**
     * 类Adaptee没有的方法
     */
    public void samplieOperation2();
}
