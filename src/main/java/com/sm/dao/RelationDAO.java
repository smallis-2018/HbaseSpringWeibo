package com.sm.dao;

import com.sm.util.HBaseCon;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RelationDAO {
    private static final String tableName = "sm:relation";

    private Table table;

    @Autowired
    public RelationDAO(HBaseCon hBaseCon) {
        try {
            this.table = hBaseCon.getConnection().getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * PUT操作
     *
     * @param rowKey     行键
     * @param familyName 列族名
     * @param columnName 列名
     * @param value      值
     */
    public void put(String rowKey, String familyName, String columnName, String value) throws IOException {
        Put p = new Put(rowKey.getBytes());
        p.addColumn(familyName.getBytes(), columnName.getBytes(), value.getBytes());
        table.put(p);
    }

    /**
     * 带过滤器的扫描
     *
     * @param filter 过滤器
     */
    public ResultScanner scan(Filter filter) throws IOException {
        Scan scan = new Scan();
        scan.setFilter(filter);
        return table.getScanner(scan);
    }

    /**
     * 带过滤器的扫描，限定扫描的列
     *
     * @param filter 过滤器
     */
    public ResultScanner scan(Filter filter, String[] familyNames, String[] columnNames) throws IOException {
        Scan scan = new Scan();
        scan.setFilter(filter);
        if (familyNames.length != columnNames.length) {
            return null;
        }
        for (int i = 0; i < familyNames.length; i++)
            scan.addColumn(familyNames[i].getBytes(), columnNames[i].getBytes());
        return table.getScanner(scan);
    }

    /**
     * 带多个过滤器的扫描，限定扫描的列族
     *
     * @param filterList 过滤器列表
     */
    public ResultScanner scan(FilterList filterList, String familyName) throws IOException {
        Scan scan = new Scan();
        scan.addFamily(familyName.getBytes());
        scan.setFilter(filterList);
        return table.getScanner(scan);
    }

    /**
     * 带多个过滤器的扫描，限定扫描的列
     *
     * @param filterList 过滤器列表
     */
    public ResultScanner scan(FilterList filterList, String familyName, String columnName) throws IOException {
        Scan scan = new Scan();
        scan.addColumn(familyName.getBytes(), columnName.getBytes());
        scan.setFilter(filterList);
        return table.getScanner(scan);
    }

    /**
     * GET操作，限定列族
     *
     * @param rowKey     行键
     * @param familyName 列族
     */
    public Result get(String rowKey, String familyName) throws IOException {
        Get get = new Get(rowKey.getBytes());
        get.addFamily(familyName.getBytes());
        return table.get(get);
    }

    /**
     * GET操作，带过滤器
     *
     * @param filter 过滤器
     */
    public Result get(String rowKey, Filter filter) throws IOException {
        Get get = new Get(rowKey.getBytes());
        get.setFilter(filter);
        return table.get(get);
    }

    /**
     * GET操作，限定列
     */
    public Result get(String rowKey, String familyName, String columnName) throws IOException {
        Get get = new Get(rowKey.getBytes());
        get.addColumn(familyName.getBytes(), columnName.getBytes());
        return table.get(get);
    }

    /**
     * 行删除
     */
    public void delete(String rowKey) throws IOException {
        Delete delete = new Delete(rowKey.getBytes());
        table.delete(delete);
    }

    /**
     * 列删除
     */
    public void delete(String rowKey, String familyName, String columnName) throws IOException {
        Delete delete = new Delete(rowKey.getBytes());
        delete.addColumn(familyName.getBytes(), columnName.getBytes());
        table.delete(delete);
    }
}
