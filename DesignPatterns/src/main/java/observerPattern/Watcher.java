package observerPattern;

import java.util.Observable;
import java.util.Observer;

public class Watcher implements Observer {

    /**
     * 构造观察者，并将观察者自己添加到被观察者的观察者obs中
     * @param o
     */
    public Watcher(Observable o){
        o.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("状态发生改变：" + ((Watched)o).getData());
    }
}
