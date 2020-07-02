package com.sm.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HBaseCon {

    private static final String IP = "192.168.37.129";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public HBaseCon() {
        Configuration configuration = HBaseConfiguration.create();
        //设置集群地址
        configuration.set("hbase.zookeeper.quorum", IP);
        try {
            //获取连接
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
