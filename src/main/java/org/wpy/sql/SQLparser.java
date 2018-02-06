package org.wpy.sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlExportParameterVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * DESC
 *
 * @author
 * @create 2017-05-31 下午4:40
 **/
public class SQLparser {
    public static void main(String[] args) {

        // String sql = "update t set name = 'x' where id < ？ and name = ? limit 10";
        // String sql = "SELECT ID, NAME, AGE FROM USER WHERE ID = ? limit 2";
        // String sql = "select * from tablename limit 10";

        String sql = "SELECT column_name(s)\n" +
                "FROM table_name1 table1\n" +
                "LEFT JOIN table_name2 table2\n" +
                "ON table2.column_name=table1.column_name  ";
        String dbType = JdbcConstants.MYSQL;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(result, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);


            //获取表名称
            System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());

            visitor.getColumns().forEach(a -> System.out.println(a.getName() + "  "));

            System.out.println("-------------------");
            visitor.getConditions().forEach(a -> System.out.println(a.getColumn().getTable() + "." + a.getColumn().getName()));


            visitor.getConditions().forEach(a -> System.out.println(a.getOperator() + a.getValues()));

            visitor.getTables().forEach((a, b) -> System.out.println(a.getName() + b));


            System.out.println("getCurrentTable：" + visitor.getCurrentTable());


            System.out.println("12312-------------------");
            List list = new ArrayList<>();
            list.add("sssss");
            list.add("name");
            MySqlExportParameterVisitor mySqlExportParameterVisitor = new MySqlExportParameterVisitor(list);
            stmt.accept(mySqlExportParameterVisitor);
            mySqlExportParameterVisitor.getParameters().stream().forEach(System.out::println);


        }

    }

}

