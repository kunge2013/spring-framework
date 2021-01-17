//package com.simple.commscollections;
//
//import org.apache.commons.collections4.map.ListOrderedMap;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author fangkun
// * @date 2020/9/9 17:32
// * @description:
// */
//public class ListOrderedMapTest {
//    public static void main(String[] args) {
//        Map<String, String> listOrderedMap = ListOrderedMap.listOrderedMap(new HashMap<String, String>());
//        listOrderedMap.put("1", "Test1");
//        listOrderedMap.put("3", "Test3");
//        listOrderedMap.put("4", "Test4");
//        listOrderedMap.put("2", "Test2");
//
//        Set<String> set2 = listOrderedMap.keySet();
//        for (String key : set2) {
//            System.out.println(key + ":" + listOrderedMap.get(key));
//        }
//
//    }
//}
