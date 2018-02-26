package org.wpy.sql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

/**
 * @Author
 * @Date 2016/11/28 13:03
 * @Describle
 */
public class JSQLTest {

    public static void main(String[] args) throws JSQLParserException {


        String sql = "SELECT\n" +
                "\tO.merchant_no AS merchantNo,\n" +
                "\tO.merchant_name as merchantName,\n" +
                "\tO.pay_type\tas TYPE,\n" +
                "\tO.order_no as orderNo,\n" +
                "\tO.request_amount as amount,\n" +
                "\tO.order_status as orderStatus,\n" +
                "\tP.pay_no as payNo,\n" +
                "\tP.order_no as orderNo ,\n" +
                "\tP.amount_success  as successAmount,\n" +
                "\tP.channel_flag AS channel,\n" +
                "\tP.merchant_rate as rate,\n" +
                "\tP.channel_rate as channelRate,\n" +
                "\tP.complete_date as complete_date\n" +
                "FROM\n" +
                "\t t_txp_order O\n" +
                "LEFT JOIN \n" +
                "        t_txp_pay P ON O.order_no = P.order_no\n" +
                "WHERE\n" +
                "\tO.create_date >= '22' and O.create_date < '323' and P.create_date >= '32323' and P.create_date < '2222'";
        Select select = (Select) CCJSqlParserUtil.parse(sql);

        //Start of value modification
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        SelectDeParser deparser = new MySelectDeParser(expressionDeParser, buffer, "ssss-wang");

        expressionDeParser.setSelectVisitor(deparser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(deparser);
        //End of value modification


        System.out.println(buffer.toString());




       /* Statement statement = CCJSqlParserUtil.parse("SELECT * FROM MY_TABLE1 left join table2 on MY_TABLE1.id = table2.id ");
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
        tableList.stream().forEach(System.out::println);
*/


    }


}