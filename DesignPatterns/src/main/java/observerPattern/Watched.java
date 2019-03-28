package observerPattern;

import java.util.Observable;

/**
 * 定义被观察者
 */
public class Watched extends Observable {
    private String data = "";


    public String getData(){
        return data;
    }

    public void setData(String data) {
        if(!this.data.equals(data)){
            this.data = data;
            //数据发生改变，变更状态标签
            setChanged();
        }
        //将状态变更通知给观察者
        notifyObservers();
    }
}
