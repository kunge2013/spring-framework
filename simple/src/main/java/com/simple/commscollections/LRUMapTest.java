package com.simple.commscollections;

import org.apache.commons.collections4.map.LRUMap;

/**
 * @author fangkun
 * @date 2020/9/9 16:38
 * @description:
 */
public class LRUMapTest {
    public static void main(String[] args) {
        LRUMap lruMap = new LRUMap(4);
        lruMap.put("a1", "1");
        lruMap.put("a2", "2");

        lruMap.get("a2");
        lruMap.get("a2");
        lruMap.get("a2");
        lruMap.get("a1");

        lruMap.put("a3", "3");
        lruMap.put("a4", "4");

        lruMap.get("a2");
        // mark as recent used
        lruMap.get("a1");
        // mark as recent used
        System.out.println(lruMap);

    }
}
