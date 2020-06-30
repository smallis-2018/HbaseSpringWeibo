package com.sm.service;

import com.sm.dao.RelationDAO;
import com.sm.util.mapUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RelationService {

    private final RelationDAO relationDAO;
    @Autowired
    public RelationService(RelationDAO relationDAO) {
        this.relationDAO = relationDAO;
    }

    public boolean doFollow(String id, String followId) {
        try {
            //取我的名字
            String myName = getName(id);
            //取要关注人的名字
            String hisName = getName(followId);
            relationDAO.put(id,"follow",followId,hisName);
            relationDAO.put(followId,"fans",id,myName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean doUnFollow(String id, String followId) {
        try {
            relationDAO.delete(id, "follow", followId);
            relationDAO.delete(followId, "fans", id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public TreeMap<String, String> AreYouAFan(String id, String fanName) {
        TreeMap<String, String> fanMap = new TreeMap<String, String>();
        TreeMap<String, String> fansMap = getFans(id);
        Set<String> keySet = fansMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            String name = fansMap.get(next);
            if (name.equals(fanName)) {
                fanMap.put(next, name);
            }
        }
        if (fanMap.isEmpty()) {
            fanMap.put("", "");
        }
        return fanMap;
    }

    public TreeMap<String, String> getUserBaseInfo(String id) {
        TreeMap<String, String> baseMap = new TreeMap<String, String>();
        try {
            Result result = relationDAO.get(id, "base");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneRow(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    baseMap.put(userId, userName);
                }
            }else {
                baseMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseMap;
    }

    //取关注列表
    public TreeMap<String,String> getFollow(String id){
        TreeMap<String,String> followMap = new TreeMap<String, String>();
        try {
            Result result = relationDAO.get(id,"follow");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneQualifier(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    if(!userId.equals("name")){
                        followMap.put(userId, userName);
                    }
                }
            }else {
                followMap.put("","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return followMap;
    }

    //取粉丝列表
    public TreeMap<String,String> getFans(String id){
        TreeMap<String,String> fansMap = new TreeMap<String, String>();
        try {
            Result result = relationDAO.get(id,"fans");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneQualifier(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    if(!userId.equals("name")){
                        fansMap.put(userId, userName);
                    }
                }
            } else {
                fansMap.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fansMap;
    }

    //todo 实现查找粉丝

    public TreeMap<String, String> getStranger(String id) {
        TreeMap<String, String> strangerMap = new TreeMap<String, String>();
        TreeMap<String, String> followMap;
        //获取关注列表
        followMap = getFollow(id);
        //设置个AND过滤器表
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //将关注列表里的ID都加入行键过滤器
        for (String s : followMap.keySet()) {
            Filter filter = new RowFilter(
                    CompareOperator.NOT_EQUAL,
                    new BinaryComparator(s.getBytes())
            );
            filterList.addFilter(filter);
        }

        //把自己的ID也放进过滤器
        Filter filter = new RowFilter(
                CompareOperator.NOT_EQUAL,
                new BinaryComparator(id.getBytes())
        );
        filterList.addFilter(filter);

        try {
            ResultScanner resultScanner = relationDAO.scan(filterList, "base", "name");
            if (resultScanner != null) {
                for (Result result:resultScanner){
                    Cell[] cells = result.rawCells();
                    for (Cell cell : cells) {
                        String getId = new String(CellUtil.cloneRow(cell));
                        String getName = new String(CellUtil.cloneValue(cell));
                        strangerMap.put(getId, getName);
                    }
                }
            } else {
                strangerMap.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //切割MAP为十个一组
        List<TreeMap<String, String>> mapList = mapUtil.mapChunk(strangerMap, 10);

        //随机取得一组数据
        Random random = new Random();
        int i = random.nextInt(mapList.size());
        strangerMap = mapList.get(i);

        return strangerMap;
    }

    public String getName(String id){
        String name = "";
        Result r1;
        try {
            r1 = relationDAO.get(id,"base","name");
            if (r1 != null) {
                Cell[] cells = r1.rawCells();
                for (Cell cell : cells) {
                    name = new String(CellUtil.cloneValue(cell));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
}
