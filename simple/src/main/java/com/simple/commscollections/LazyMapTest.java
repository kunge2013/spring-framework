//package com.simple.commscollections;
//
//import org.apache.commons.collections4.map.LazyMap;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author fangkun
// * @date 2020/9/9 17:12
// * @description:
// */
//public class LazyMapTest {
//    public static void main(String[] args) {
//        org.apache.commons.collections4.Factory<Long> factory = new org.apache.commons.collections4.Factory() {
//            @Override
//            public Long create() {
//                return System.nanoTime();
//            }
//        };
//        Map<String, Long> lazy = LazyMap.lazyMap(new HashMap<String, Long>(), factory);
//        System.out.println("map:" + lazy);//lazy为空
//        System.out.println(lazy.get("123"));
//        System.out.println(lazy.get("345"));
//
//        System.out.println(lazy.get("123"));
//        System.out.println(lazy.get("345"));
//        System.out.println("map:" + lazy);//lazy为空
//    }
//}
