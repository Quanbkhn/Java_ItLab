/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topica.itlab.java8.sort;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class ParallelismJava8 {

    public static void main(String[] args) {

        ArrayList<DemoSortJava8.Student> listStudent = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoSortJava8.Student student = new DemoSortJava8.Student("Student" + i, new Random().nextInt(100));
            listStudent.add(student);
        }

        long startTimeWithParallel = System.nanoTime();
        listStudent.parallelStream().filter((t) -> t.getPoint().equals(50)).count();
        long endTimeWithParallel = System.nanoTime();
        System.out.println(" Time count with parrallel : " + (endTimeWithParallel - startTimeWithParallel)+ " nano ");

        long startTime = System.nanoTime();
        listStudent.stream().filter((t) -> t.getPoint().equals(50)).count();
        long endTime = System.nanoTime();
        System.out.println(" Time count withOut parrallel : " + (endTime - startTime)+ " nano ");

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
