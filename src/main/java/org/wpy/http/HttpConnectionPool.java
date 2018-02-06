package org.wpy.http;


import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;


/**
 * @author wpy
 * @version V1.0
 * @Description: TODO
 * @Package org.wpy.http
 * @date 2017/12/27 21:55
 * <p>
 * org.apache.http.impl.execchain.MainClientExec#execute(org.apache.http.conn.routing.HttpRoute, org.apache.http.client.methods.HttpRequestWrapper, org.apache.http.client.protocol.HttpClientContext, org.apache.http.client.methods.HttpExecutionAware)
 * <p>
 * 包含了http  leaseConnection和releaseConnection( 空或者Stream 为空)
 * <p>
 * 归还 http connect 给 Cpool 池： org.apache.http.impl.execchain.ConnectionHolder#releaseConnection(boolean)
 */
public class HttpConnectionPool {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient closeableHttpClient;
        CloseableHttpResponse httpResponse = null;
        try {
            closeableHttpClient = acceptsUntrustedCertsHttpClient(null, null);
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            httpResponse = closeableHttpClient.execute(httpGet);
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭stream流，但不是关闭链接，链接归还给pool
            if (httpResponse != null) {
                EntityUtils.consume(httpResponse.getEntity());
            }
        }
    }

    /**
     * 得到http链接，但不能close关闭
     * <p>
     * connect : 链接链路
     * socket ：建立数据通信的一端（例如服务器）
     *
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws CertificateException
     */
    public static CloseableHttpClient acceptsUntrustedCertsHttpClient(File keyStore, String password)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException, CertificateException {

        //获得密匙库
        KeyStore trustStore = null;
        if (Objects.nonNull(keyStore) && keyStore.exists()) {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream fileInputStream = FileUtils.openInputStream(keyStore);
            trustStore.load(fileInputStream, password.toCharArray());
            fileInputStream.close();
        }

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStore, (arg0, arg1) -> true).build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(200);               //最大链接数
        connMgr.setDefaultMaxPerRoute(100);     //每个域名可有最大的链接数


        /** http请求参数配置 **/
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(2 * 1000)   //从连接池中得到链接等待时间
                .setConnectTimeout(2 * 1000)             //建立链接时间
                .setSocketTimeout(5 * 1000)               //网络上的两个程序通过一个双向的通信连接实现数据的交换，这个连接的一端称为一个socket。
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .setConnectionManager(connMgr)
                .setDefaultRequestConfig(requestConfig)
                .build();

        /**
         * JVM 关闭，则关闭http链接
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        return client;
    }

}
