package org.wpy.sql;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

/**
 * DESC
 *
 * @author
 * @create 2017-06-07 上午11:52
 **/
public class MySqlSchemaStatVisitor2 extends MySqlSchemaStatVisitor {

    @Override
    public boolean visit(SQLExprTableSource x) {
        return super.visit(x);
    }
}
