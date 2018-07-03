package com.topica.itlab.java8.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DemoSortJava8 {

    public static void main(String[] args) {

        ArrayList<Student> listStudent = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student("Student" + i, new Random().nextInt(100));
            listStudent.add(student);
        }

        // Sort in java 8
        Collections.sort(listStudent, (o1, o2) -> {
            return (o2.getPoint() - o1.getPoint());
        });

        listStudent.forEach((t) -> {
            t.showInfo();
        });
    }

    public static class Student {

        private String name;
        private Integer point;

        public Student() {
        }

        public Student(String name, Integer point) {
            this.name = name;
            this.point = point;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        public void showInfo() {
            System.out.println(this.name + " : " + this.point);
        }

    }
}
