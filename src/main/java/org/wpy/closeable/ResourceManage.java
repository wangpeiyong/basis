package org.wpy.closeable;

import java.io.IOException;

/**
 * @author wpy
 * @version V1.0
 * @Description: Closeable实现的 try()模式，自动释放close方法。
 * @Package org.wpy.closeable
 * @date 2018/1/25 23:07
 */
public class ResourceManage {

    /**
     * closeable try() 自动释放资源
     *
     * @param args
     */
    public static void main(String[] args) {
        try (ResourceClient resourceClient = new ResourceClient();
             ResourceClient resourceClient2 = new ResourceClient();) {
            resourceClient.doInvoke("wang");
            resourceClient2.doInvoke("wang2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
