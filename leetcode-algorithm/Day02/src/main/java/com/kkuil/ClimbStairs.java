package com.kkuil;

/**
 * @Author Kkuil
 * @Date 2023/8/13 17:37
 * @Description 爬楼梯 - 动态规划
 */
public class ClimbStairs {
    public static void main(String[] args) {
        System.out.println("climbStairs(3) = " + climbStairs(3));
    }

    /**
     * @param n 楼梯数
     * @return 爬楼梯的方法数
     */
    public static int climbStairs(int n) {
        int p = 0, q = 0, r = 1;
        for (int i = 1; i <= n; ++i) {
            p = q;
            q = r;
            r = p + q;
        }
        return r;
    }
}
