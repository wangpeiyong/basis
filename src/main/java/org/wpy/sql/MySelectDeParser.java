package org.wpy.sql;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

/**
 * @Author chenxl
 * @Date 2016/11/28 13:27
 * @Describle
 */
public class MySelectDeParser extends SelectDeParser {

    private String table;

    public MySelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, String tableName) {
        super(expressionVisitor, buffer);
        this.table = tableName;
    }


    @Override
    public void visit(WithItem withItem) {
        System.out.println("withItem" + withItem);
        super.visit(withItem);
    }

    @Override
    public void visit(Table tableName) {
       /*

        System.out.println("getSchemaName:" + tableName.getSchemaName());
        String schema = tableName.getSchemaName();
        tableName.setSchemaName("wang" + "." + schema);

        */
        if (tableName.getName().equals("MY_TABLE1"))
            tableName.setName(table);

        StringBuilder buffer = getBuffer();
        buffer.append(tableName.getFullyQualifiedName());
        Pivot pivot = tableName.getPivot();


        if (pivot != null) {
            pivot.accept(this);
        }
        Alias alias = tableName.getAlias();
        if (alias != null) {
            buffer.append(alias);
        }
    }


    @Override
    public void visit(PlainSelect plainSelect) {
        System.out.println(plainSelect.toString());
        super.visit(plainSelect);
    }

    @Override
    public void visit(Column column) {
        System.out.println(column.toString());
        super.visit(column);
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        System.out.println(allTableColumns.toString());

        super.visit(allTableColumns);
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        System.out.println(selectExpressionItem.toString());

        super.visit(selectExpressionItem);
    }

    @Override
    public void visit(SubSelect subSelect) {
        System.out.println(subSelect.toString());

        super.visit(subSelect);
    }

    @Override
    public void visit(Pivot pivot) {
        System.out.println(pivot.toString());
        super.visit(pivot);
    }

    @Override
    public void visit(PivotXml pivot) {
        System.out.println(pivot.toString());
        super.visit(pivot);
    }

    @Override
    public void deparseOffset(Offset offset) {
        System.out.println(offset.toString());
        super.deparseOffset(offset);
    }

    @Override
    public void deparseFetch(Fetch fetch) {
        System.out.println(fetch.toString());
        super.deparseFetch(fetch);
    }

    @Override
    public StringBuilder getBuffer() {
        return super.getBuffer();
    }

    @Override
    public void setBuffer(StringBuilder buffer) {
        System.out.println(buffer.toString());
        super.setBuffer(buffer);
    }

    @Override
    public ExpressionVisitor getExpressionVisitor() {
        return super.getExpressionVisitor();
    }

    @Override
    public void setExpressionVisitor(ExpressionVisitor visitor) {
        System.out.println(visitor.toString());
        super.setExpressionVisitor(visitor);
    }

    @Override
    public void visit(SubJoin subjoin) {
        System.out.println(subjoin.toString());
        super.visit(subjoin);
    }

    @Override
    public void deparseJoin(Join join) {
        System.out.println(join.toString());
        super.deparseJoin(join);
    }

    @Override
    public void visit(SetOperationList list) {
        System.out.println(list.toString());
        super.visit(list);
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        System.out.println(lateralSubSelect.toString());
        super.visit(lateralSubSelect);
    }

    @Override
    public void visit(ValuesList valuesList) {
        System.out.println(valuesList.toString());
        super.visit(valuesList);
    }

    @Override
    public void visit(AllColumns allColumns) {
        System.out.println(allColumns.toString());
        super.visit(allColumns);
    }

    @Override
    public void visit(TableFunction tableFunction) {
        System.out.println(tableFunction.toString());
        super.visit(tableFunction);
    }
}
