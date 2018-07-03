/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topica.itlab.java8.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class Java8MapToList {

    public static void main(String[] args) {

        Map<Integer, String> map = new HashMap<>();
        map.put(10, "Java");
        map.put(30, "Python");
        map.put(40, "CSharp");
        map.put(20, "GoLang");
        map.put(50, "Ruby");

        System.out.println("\n1. Export Map Key to List...");
        List<Integer> listKey = map.keySet().stream()
                .collect(Collectors.toList());
        listKey.forEach(System.out::println);

        System.out.println("\n2. Export Map Value to List...");
        List<String> listValue = map.values().stream()
                .collect(Collectors.toList());
        listValue.forEach(System.out::println);

        System.out.println("\n3. Export Map Value to List..., without Ruby ");
        List<String> result = map.values().stream()
                .filter(x -> !"Ruby".equalsIgnoreCase(x))
                .collect(Collectors.toList());

        result.forEach(System.out::println);
    }

}
