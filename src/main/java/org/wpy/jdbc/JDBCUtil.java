package org.wpy.jdbc;

import com.alibaba.druid.proxy.DruidDriver;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    /*static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    static {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            DruidDriver.registerDriver(new DruidDriver());
            return null;
        });
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://172.19.110.233:3306/platform_partner?useUnicode=true&amp;characterEncoding=utf-8");
    }
}
