/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topica.itlab.java8.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class Java8ListToMap {

    public static void main(String[] args) {

        List<Student> listStudent = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student("Student" + i, new Random().nextInt(100));
            listStudent.add(student);
        }
        listStudent.forEach((t) -> {
            t.showInfo();
        });

        Map<Integer, String> mapStudent = listStudent.stream().collect(
                Collectors.toMap(Student::getId, Student::getName, // key = id, value = name
                        (oldValue, newValue) -> newValue // if same key, take the new key
                ));
        System.out.println(mapStudent);

    }

    public static class Student {

        private String name;
        private Integer id;

        public Student() {
        }

        public Student(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void showInfo() {
            System.out.println(this.name + " : " + this.id);
        }

    }

}
