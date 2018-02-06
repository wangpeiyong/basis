package org.wpy.observer;


import java.util.Observable;
import java.util.Observer;

/**
 * DESC   观察者模式：
 * <p>
 * 在被观察者中，有多个观察者的引用。notifyObservers()调用观察者的update方法，实现时间的发送。
 *
 * @author
 * @create 2017-07-13 下午4:06
 **/
public class MyObserver {

    public static void main(String[] args) throws Exception {
        Subject subject = new Subject("-------data-------");

        //将观察者添加到被观察者中。
        Subscribe subscribe1 = new Subscribe();
        Subscribe subscribe2 = new Subscribe();
        subject.addObserver(subscribe1);
        subject.addObserver(subscribe2);

        //触发被观察的事件
        subject.createMsg();
    }
}


/**
 * 被观察者  Observable 可被观察的
 */
class Subject extends Observable {
    private String data;

    public Subject(String data) {
        this.data = data;
    }

    public void createMsg() {
        //只有在setChange()被调用后，notifyObservers()才会去调用update()，否则什么都不干。
        super.setChanged();
        notifyObservers("-------createMsg-------" + data);
    }

    @Override
    public String toString() {
        return "Subject{" + "data='" + data + '\'' + '}';
    }
}


/**
 * 观察者接口 Observer
 */
class Subscribe implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Data has changed to" + o.toString() + "  " + arg);
    }
}

