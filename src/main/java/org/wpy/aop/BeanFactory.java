package org.wpy.aop;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author wangpeiyong
 * @Date 2018/2/9 11:33
 */
public class BeanFactory {
    private static Properties properties = new Properties();
    static {
        try {
            InputStream is = new FileInputStream("/Users/wpy/Documents/java/open-open/basis/src/resource/aop.properties");
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String name) {
        String beanStr = properties.getProperty(name);
        try {
            Object bean = Class.forName(beanStr).newInstance();
            if (bean instanceof ProxyFactoryBean) {
                ProxyFactoryBean proxyFactoryBean = (ProxyFactoryBean) bean;
                String targetStr = properties.getProperty(name + ".target");
                String adviceStr = properties.getProperty(name +".advice");
                Object target = Class.forName(targetStr).newInstance();
                IAdvice advice = (IAdvice) Class.forName(adviceStr).newInstance();
                proxyFactoryBean.setAdvice(advice);
                proxyFactoryBean.setTarget(target);
                return proxyFactoryBean.getProxy();
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
