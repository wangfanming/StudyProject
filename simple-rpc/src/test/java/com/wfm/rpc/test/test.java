package com.wfm.rpc.test;

/**
 * @ClassName: test
 * @Auther: wangfanming
 * @Date: 2019/8/31
 * @Description: TODO
 * @Version 1.0
 */
public class test {
    private static boolean isPowerOfTwo(int val) {
        int i = val & -val;
        System.out.println(i);
        return (val & -val) == val;
    }
    public static void main(String[] args) {
        System.out.println(isPowerOfTwo(4));
    }
}
