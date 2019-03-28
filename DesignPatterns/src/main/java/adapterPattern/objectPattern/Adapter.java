package adapterPattern.objectPattern;

public class Adapter {
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }

    /**
     * samplieOperation1()方法在Adaptee已经有了，直接委派
     */
    public void  samplieOperation1(){
        adaptee.samplieOperation1();
    }

    /**
     * Adaptee中没有samplieOperation2(),因此由适配器类提供实现
     */
    public void samplieOperation2(){

    }
}
