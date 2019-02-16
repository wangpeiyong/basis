package org.wpy.foreach;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyInterator implements Iterable {

    private String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer action) {
        for (String name : names) {
            action.accept(name);
        }
    }

    @Override
    public Spliterator spliterator() {
        return null;
    }

    public static void main(String[] args) {
        MyInterator myInterator = new MyInterator();
        myInterator.setNames(new String[]{"wpy", "zs"});
        myInterator.forEach(m-> {
            System.out.println(m);
        });
    }
}