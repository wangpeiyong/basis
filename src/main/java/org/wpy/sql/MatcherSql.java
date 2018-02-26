package org.wpy.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DESC
 *
 * @author
 * @create 2017-06-07 下午4:22
 **/
public class MatcherSql {
    public static void main(String[] args) {

        //SELECT 列名称（*所有列） FROM 表名称
        //SELECT 列名称 FROM 表名称 where 条件
        System.out.println(matchSql("select * from aaa "));

        System.out.println(matchSql("SELECT column_name(s) " +
                "FROM table_name1 table1 " +
                "LEFT JOIN table_name2 table2 " +
                "ON  table2.column_name=table1.column_name  " +
                " where s =aa"));

        System.out.println(matchSql("select id,name,password from bbb where id = 1 "));
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
        System.out.println(matchSql("insert into ccc values(1,'neo','password')"));
        System.out.println(matchSql("insert into ddd (id,name,password) values(1,'neo','password')"));
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        System.out.println(matchSql("update eee set name = 'neo' where id = 1 "));
        //DELETE FROM 表名称 WHERE 列名称 = 值
        System.out.println(matchSql("delete from fff where id = 1 "));

       /* String sql = "delete from fff where id = 1 ";
        String changedSql = changeDelete(sql);
        System.out.println(changedSql);*/
    }

    /**
     * @param sql lowcase
     * @return
     */
    public static String matchSql(String sql) {
        Matcher matcher = null;
        //SELECT 列名称 FROM 表名称
        //SELECT * FROM 表名称
        sql = sql.toLowerCase();
        if (sql.startsWith("select")) {
            matcher = Pattern.compile("select\\s.+from\\s(.+)where\\s(.*)").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
        if (sql.startsWith("insert")) {
            matcher = Pattern.compile("insert\\sinto\\s(.+)\\(.*\\)\\s.*").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        if (sql.startsWith("update")) {
            matcher = Pattern.compile("update\\s(.+)set\\s.*").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        //DELETE FROM 表名称 WHERE 列名称 = 值
        if (sql.startsWith("delete")) {
            matcher = Pattern.compile("delete\\sfrom\\s(.+)where\\s(.*)").matcher(sql);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}