package me.koply.pikap.test;

import me.koply.pikap.util.FixedStack;

public class Test {

    public static void main(String[] args) {
        FixedStack<Integer> fixedStack = new FixedStack<>(5);
        fixedStack.addAll(1,2,3,4,5,6);

        fixedStack.forEach(System.out::print);
        System.out.println();
        for (int i : fixedStack) {
            System.out.print(i);
        }
    }
}