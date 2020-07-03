package com.sm.service;

import com.sm.dao.RelationDAO;
import com.sm.util.mapUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

@Service
public class RelationService {

    private final RelationDAO relationDAO;

    @Autowired
    public RelationService(RelationDAO relationDAO) {
        this.relationDAO = relationDAO;
    }

    /***
     * 实现关注业务
     * @param id 关注者id
     * @param followId 被关注者id
     */
    public boolean doFollow(String id, String followId) {
        try {
            //关注者的名字
            String myName = getName(id);
            //被关注者的名字
            String hisName = getName(followId);
            //记录关注信息
            relationDAO.put(id, "follow", followId, hisName);
            //记录粉丝信息
            relationDAO.put(followId, "fans", id, myName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /***
     * 实现取消关注业务
     * @param id 关注者id
     * @param followId 被关注者id
     */
    public boolean doUnFollow(String id, String followId) {
        try {
            //删除关注信息
            relationDAO.delete(id, "follow", followId);
            //删除粉丝信息
            relationDAO.delete(followId, "fans", id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 搜索用户
     *
     * @param name 用户名字
     * @param maps 要查找的列表
     */
    public TreeMap<String, String> findUser(String name, TreeMap<String, String> maps) {
        TreeMap<String, String> map = new TreeMap<String, String>();

        //设置个AND过滤器表
        FilterList andFilterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //设置个OR过滤器表
        FilterList orFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        //将列表里的ID都加入行键过滤器
        for (String s : maps.keySet()) {
            //行键过滤器
            Filter filter = new RowFilter(
                    CompareOperator.EQUAL,
                    new BinaryComparator(s.getBytes())
            );
            //满足一个ID就行
            orFilterList.addFilter(filter);
        }

        //名字单值模糊过滤
        Filter filter = new SingleColumnValueFilter(
                "base".getBytes(),
                "name".getBytes(),
                CompareOperator.EQUAL,
                new SubstringComparator(name)
        );
        //ID和名字同时满足
        andFilterList.addFilter(filter);
        andFilterList.addFilter(orFilterList);

        try {
            ResultScanner resultScanner = relationDAO.scan(andFilterList, "base", "name");
            if (resultScanner != null) {
                for (Result result : resultScanner) {
                    Cell[] cells = result.rawCells();
                    for (Cell cell : cells) {
                        String getId = new String(CellUtil.cloneRow(cell));
                        String getName = new String(CellUtil.cloneValue(cell));
                        map.put(getId, getName);
                    }
                }
            } else {
                map.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 实现搜索粉丝列表的业务
     *
     * @param id      我的id
     * @param fanName 粉丝名字
     */
    public TreeMap<String, String> AreYouAFan(String id, String fanName) {
        //获取我的粉丝列表
        TreeMap<String, String> fansMap = getFans(id);

        return findUser(fanName, fansMap);
    }

    /**
     * 实现搜索陌生人列表的业务
     *
     * @param id   我的id
     * @param name 粉丝名字
     */
    public TreeMap<String, String> AreYouNotAFan(String id, String name) {

        TreeMap<String, String> strangerMap = getStranger(id);

        return findUser(name, strangerMap);
    }


    /**
     * 实现获取用户基本信息业务
     */
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
            } else {
                baseMap.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseMap;
    }

    /**
     * 实现获取关注列表业务
     */
    public TreeMap<String, String> getFollow(String id) {
        TreeMap<String, String> followMap = new TreeMap<String, String>();
        try {
            Result result = relationDAO.get(id, "follow");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneQualifier(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    if (!userId.equals("name")) {
                        followMap.put(userId, userName);
                    }
                }
            } else {
                followMap.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return followMap;
    }

    /**
     * 实现获取粉丝列表业务
     */
    public TreeMap<String, String> getFans(String id) {
        TreeMap<String, String> fansMap = new TreeMap<String, String>();
        try {
            Result result = relationDAO.get(id, "fans");
            if (result != null) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    String userId = new String(CellUtil.cloneQualifier(cell));
                    String userName = new String(CellUtil.cloneValue(cell));
                    if (!userId.equals("name")) {
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

    /**
     * 实现随机获取没有关注的人的业务，一次返回十个人
     */
    public TreeMap<String, String> getStranger(String id) {
        TreeMap<String, String> strangerMap = new TreeMap<String, String>();
        TreeMap<String, String> followMap;
        //获取关注列表
        followMap = getFollow(id);
        //设置个AND过滤器表
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //将关注列表里的ID都加入行键过滤器
        for (String s : followMap.keySet()) {
            //行键过滤器
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

        return strangerMap;
    }

    public TreeMap<String, String> followBackMap(String id) {
        TreeMap<String, String> map, followMap;

        map = new TreeMap<String, String>();

        /**
         * 1、查询关注的用户ID
         * 2、根据用户ID在粉丝列族中查找相同ID的人
         */

        //设置个AND过滤器表
        FilterList andFilterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //设置个OR过滤器表
        FilterList orFilterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        orFilterList.addFilter(new QualifierFilter(
                CompareOperator.EQUAL,
                new BinaryComparator("".getBytes())
        ));

        followMap = getFollow(id);
        //将列表里的ID都加入过滤器
        for (String s : followMap.keySet()) {
            //列值过滤器
            Filter filter = new QualifierFilter(
                    CompareOperator.EQUAL,
                    new BinaryComparator(s.getBytes())
            );
            orFilterList.addFilter(filter);
        }

        Filter filter = new RowFilter(
                CompareOperator.EQUAL,
                new BinaryComparator(id.getBytes())
        );

        andFilterList.addFilter(filter);
        andFilterList.addFilter(orFilterList);

        try {
            //扫描Fans列族
            ResultScanner resultScanner = relationDAO.scan(andFilterList, "fans");
            if (resultScanner != null) {
                for (Result result : resultScanner) {
                    Cell[] cells = result.rawCells();
                    for (Cell cell : cells) {
                        String userId = new String(CellUtil.cloneQualifier(cell));
                        String userName = new String(CellUtil.cloneValue(cell));
                        if (!userId.equals("name")) {
                            map.put(userId, userName);
                        }
                    }
                }
            } else {
                map.put("", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public TreeMap<String, String> remove(TreeMap<String, String> toMap, TreeMap<String, String> fromMap) {
        for (String key : fromMap.keySet()) {
            toMap.remove(key);
        }
        return toMap;
    }

    public TreeMap<String, String> getFansNoFollow(String id) {
        return remove(getFans(id), followBackMap(id));
    }

    public TreeMap<String, String> getFollowNoFans(String id) {
        return remove(getFollow(id), followBackMap(id));
    }

    /**
     * 实现获取用户名的业务
     */
    public String getName(String id) {
        String name = "";
        Result r1;
        try {
            r1 = relationDAO.get(id, "base", "name");
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

    public TreeMap<String, String> getMapPage(TreeMap<String, String> map, Integer page) {
        //切割MAP为十个一组
        List<TreeMap<String, String>> mapList = mapUtil.mapChunk(map, 10);

        return mapList.get(page);
    }
}
