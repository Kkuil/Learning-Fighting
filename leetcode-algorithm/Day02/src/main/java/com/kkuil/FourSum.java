package com.kkuil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/8/12 23:57
 * @Description 四数之和
 */
public class FourSum {
    public static void main(String[] args) throws IOException {
        System.out.println("fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0) = " + fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0));
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Total memory: " + runtime.totalMemory());
        System.out.println("Free memory: " + runtime.freeMemory());
        long execMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Execute memory: " + execMemory);
        System.out.println("Available processors: " + runtime.availableProcessors());
    }

    /**
     * @param nums   数组
     * @param target 目标值
     * @return java.util.List<java.util.List < java.lang.Integer>>
     * @description 四数之和
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        ArrayList<List<Integer>> list = new ArrayList<>();
        int length = nums.length;
        // 1. 如果数组长度小于4，直接返回空列表
        if (length < 4) {
            ArrayList<Integer> emptyList = new ArrayList<>();
            list.add(emptyList);

            return list;
        }
        // 2. 对数组进行排序，方便后续去重操作（从小到大）
        Arrays.sort(nums);
        System.out.println("Arrays.toString(nums) = " + Arrays.toString(nums));
        // 3. 遍历数组，固定两个数，双指针查找另外两个数（四指针法）
        int left = 0;
        int mLeft = left + 1;
        int mRight = mLeft + 1;
        int right = length - 1;
        while (left < right || right - left < 3) {

        }

        return list;
    }

    // 题解
    public static List<List<Integer>> fourSumSolution(int[] nums, int target) { // O(n^3)
        List<List<Integer>> res = new ArrayList<>();

        Arrays.sort(nums); // O(nlogn)
        int len = nums.length;
        for (int i = 0; i < len - 3; i++) {   // O(n^3)
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue; // 跳过重复
            }
            for (int j = i + 1; j < len - 2; j++) { // same as threeSum  O(n^2)
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue; // 跳过重复
                }
                int left = j + 1;
                int right = len - 1;
                while (left < right) {
                    int sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        res.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));

                        // 跳过重复, 可以先不看
                        while (left < right && nums[left + 1] == nums[left]) {
                            left++;
                        }
                        while (left < right && nums[right - 1] == nums[right]) {
                            right--;
                        }

                        // 逼近中间
                        left++;
                        right--;
                    } else if (sum > target) {
                        right--;
                    } else { // sum < target
                        left++;
                    }
                }
            }
        }

        return res;
    }
}
