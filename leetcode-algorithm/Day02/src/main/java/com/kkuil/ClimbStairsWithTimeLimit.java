package com.kkuil;

/**
 * @Author Kkuil
 * @Date 2023/8/13 17:37
 * @Description 爬楼梯
 */
public class ClimbStairsWithTimeLimit {
    public static void main(String[] args) {
        System.out.println("climbStairs(3) = " + climbStairs(3));
    }

    /**
     * @param n 楼梯数
     * @return 爬楼梯的方法数
     */
    public static int climbStairs(int n) {
        if (n < 0) {
            return 0;
        } else if (n == 0) {
            return 1;
        }
        return climbStairs(n - 1) + climbStairs(n - 2);
    }
}
