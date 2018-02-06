package org.wpy.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description:
 * @Package org.wpy.spi
 * @date 2017/12/4 15:46
 * @see
 */
public class Main {

    public static void main(String[] args) {
        ServiceLoader<IBaseService> load = ServiceLoader.load(IBaseService.class);
        Iterator<IBaseService> iterator = load.iterator();
        while (iterator.hasNext()) {
            IBaseService next = iterator.next();
            System.out.println(next.echo("wang"));
        }
        System.out.println(loadService(ToLowerCaseBaseService.class).echo("liSi"));
    }


    public static <T extends IBaseService> IBaseService loadService(Class<T> t) {
        ServiceLoader<IBaseService> load = ServiceLoader.load(IBaseService.class);
        Iterator<IBaseService> iterator = load.iterator();
        while (iterator.hasNext()) {
            IBaseService next = iterator.next();
            if (next.getClass().equals(t))
                return next;
        }
        throw new IllegalArgumentException("未得到SPI服务实例");
    }
}
