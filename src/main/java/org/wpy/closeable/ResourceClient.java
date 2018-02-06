package org.wpy.closeable;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author wpy
 * @version V1.0
 * @Description: TODO
 * @Package org.wpy.closeable
 * @date 2018/1/25 23:05
 */
public class ResourceClient implements Closeable {

    public void doInvoke(String args) {
        System.out.println("================args:" + args.toUpperCase() + "================");
    }

    @Override
    public void close() throws IOException {
        System.err.println("================close================");
    }
}
