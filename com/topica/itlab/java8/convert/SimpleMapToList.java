
package com.topica.itlab.java8.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleMapToList {

    public static void main(String[] args) {

        Map<Integer, String> map = new HashMap<>();
        map.put(10, "Java");
        map.put(30, "Python");
        map.put(40, "CSharp");
        map.put(20, "GoLang");
        map.put(50, "Ruby");
        System.out.println(map);
        
        System.out.println("\n1. Export Map Key to ListKey");
        List<Integer> listKey = new ArrayList(map.keySet());
        listKey.forEach(System.out::println);
        
        System.out.println("\n2. Export Map Value to ListValue");
        List<String> listValue = new ArrayList(map.values());
        listValue.forEach(System.out::println);

    }

}
