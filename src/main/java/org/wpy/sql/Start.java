package org.wpy.sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * DESC
 *
 * @author
 * @create 2017-06-08 下午2:20
 **/
public class Start {
    public static void main(String[] args) {
        /*List<String> splitSqls = new ArrayList<>();
        List<String> InvalidSqls = new ArrayList<>();


        for (int i = 0; i <10 ; i++) {
            splitSqls.add("zhangsan"+i);
        }


        for (int i = 0; i < splitSqls.size() ;i++){

            if (i %2 ==0 && !InvalidSqls.contains(splitSqls.get(i))){
                InvalidSqls.add("zhangsan" + (i+2));
                splitSqls.add("lisi"+ i);
            }
        }



        splitSqls.forEach(System.out::println);
*/


        String sql = "SELECT O.merchant_no AS merchantNo, O.merchant_name as merchantName, O.pay_type as TYPE, O.order_no as orderNo, O.request_amount as amount, O.order_status as orderStatus, P.pay_no as payNo, P.order_no as orderNo , P.amount_success as successAmount, P.channel_flag AS channel, P.merchant_rate as rate, P.channel_rate as channelRate, P.complete_date as complete_date FROM t_txp_order O LEFT JOIN t_txp_pay P ON O.order_no = P.order_no WHERE O.create_date >= '2017-06-11' and O.create_date < '2017-06-12' and P.create_date >= '2017-05-01 00:00:00' and P.create_date < '2017-05-11 00:00:00'";
        /*String dbType = JdbcConstants.MYSQL;
        String result = SQLUtils.format(sql, dbType);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            List<String> set= visitor.getTables().keySet().stream().map(a->a.getName()).collect(Collectors.toList());
            visitor.getAliasMap().forEach((a,b) -> {
                if(set.contains(b)){
                    System.out.println( a +"  "+b);
                }
            });
            visitor.getConditions().stream().map(a->a.getColumn().getTable()).forEach(System.out::println);
        }
        System.out.println(new SimpleDateFormat("yyyyMMdd").format(new Date()));*/


        getTableAliasMap(sql).forEach((a, b) -> System.out.println(a + "   " + b));


        String patter = "(?i)p.create_date\\s*>=\\s*'[\\s\\S]*?'";
        Matcher matcher = Pattern.compile(patter).matcher(sql);
        if (matcher.find()) {
            System.out.println(matcher.replaceAll(String.format(" %s %s '%s' ", "sss", "wwwwwwwwww", "222222222222222222222")));
        }


        //"\\w+(E|e)xception:"
    }


    protected static Map<String, String> getTableAliasMap(String sql) {
        String dbType = JdbcConstants.MYSQL;
        String result = SQLUtils.format(sql, dbType);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            List<String> set = visitor.getTables().keySet().stream().map(a -> a.getName()).collect(Collectors.toList());
            visitor.getAliasMap().forEach((a, b) -> {
                if (!a.equals(b) && set.contains(b)) {
                    map.put(b, a);
                }
            });
        }
        return map;
    }
}
