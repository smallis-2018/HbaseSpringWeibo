package com.sm.util;

import java.util.*;

public class mapUtil {

    public static <k, v> List<TreeMap<k, v>> mapChunk(TreeMap<k, v> chunkMap, int chunkNum) {
        if (chunkMap == null || chunkNum <= 0) {
            List<TreeMap<k, v>> list = new ArrayList<TreeMap<k, v>>();
            list.add(chunkMap);
            return list;
        }
        Set<k> keySet = chunkMap.keySet();
        Iterator<k> iterator = keySet.iterator();
        int i = 1;
        List<TreeMap<k, v>> total = new ArrayList<TreeMap<k, v>>();
        TreeMap<k, v> tem = new TreeMap<k, v>();
        while (iterator.hasNext()) {
            k next = iterator.next();
            tem.put(next, chunkMap.get(next));
            if (i == chunkNum) {
                total.add(tem);
                tem = new TreeMap<k, v>();
                i = 0;
            }
            i++;
        }
        if (!tem.isEmpty()) {
            total.add(tem);
        }
        return total;
    }
}
