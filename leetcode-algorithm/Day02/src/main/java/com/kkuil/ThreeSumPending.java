package com.kkuil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/8/13 11:13
 * @Description 三数之和
 */
public class ThreeSumPending {
    public static void main(String[] args) {
        System.out.println("threeSum(new int[]{-1, 0, 1, 2, -1, -4}) = " + threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }

    /**
     * @param ints   数组
     * @param target 目标值
     * @return java.lang.String
     * @description 三数之和
     */
    private static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        if (nums.length < 3) {
            return list;
        }
        // 排序
        Arrays.sort(nums);

        // 三指针
        int left = 0;
        int middle = left + 1;
        int right = nums.length - 1;

        // 遍历数组
        return list;
    }
}
