package com.example.demosmali;

public class Simple {

    public Simple( ) {
        // Empty
    }

    private String name;
    private static int counter;

    public Simple(String name) {
        this.name = name;
    }

    int addIntCalc(int i, int j) {
        int k = i + j;
        return k;
    }
    int sqrtCalc(int i) {
        int ret = (int)Math.sqrt(i);
        return ret;
    }
    // NOT THREADSAFE
    int nextCount() {
        return ++counter;
    }

    public void ifTest(int i) {
        int val = 42;
        if (i > val) {
            System.out.println("High");
        }
    }

    public void  createAndCall() {
        var sb = new
                StringBuilder("Hello");
        System.out.println(
                sb.length());
    }
}