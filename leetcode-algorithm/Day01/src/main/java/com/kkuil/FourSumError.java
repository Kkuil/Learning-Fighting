package com.kkuil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Kkuil
 * @Date 2023/8/12 23:57
 * @Description 四数之和
 */
public class FourSumError {
    public static void main(String[] args) {
        System.out.println("fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0) = " + fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0));
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

        // 3. 遍历数组，固定两个数，双指针查找另外两个数（四指针法）
        int left = 0;
        int mLeft = left + 1;
        int mRight = mLeft + 1;
        int right = length - 1;
        while (left < right) {
            // 3.1 计算当前总和
            int sum = nums[left] + nums[mLeft] + nums[mRight] + nums[right];
            if (sum == target) {
                // 3.1.1 如果当前总和等于目标值，添加到列表中
                ArrayList<Integer> newList = new ArrayList<>();
                newList.add(nums[left]);
                newList.add(nums[mLeft]);
                newList.add(nums[mRight]);
                newList.add(nums[right]);
                list.add(newList);

                // 3.1.2 移动左指针，并且移动左指针后，如果左指针和左指针的下一个数相等，继续移动左指针
                // 3.1.2.1 保存当前左指针的值
                int tempLeft = nums[left];
                // 3.1.2.2 循环移动左指针
                for (int i = ++left; i < length - 1; i++) {
                    if (nums[i] != tempLeft) {
                        left = i;
                        break;
                    }
                }
                if (left >= right) {
                    break;
                }
                // 3.1.3 初始中间左指针和中间右指针
                mLeft = left + 1;
                mRight = mLeft + 1;
            } else if (sum < target) {
                // 3.1.2 如果当前总和小于目标值，移动左指针,并且移动左指针后，如果左指针和左指针的下一个数相等，继续移动左指针
                // 3.1.2.1 保存当前中间左指针的值
                int tempMLeft = nums[mLeft];
                // 3.1.2.2 循环移动中间左指针
                for (int i = ++mLeft; i < length - 1; i++) {
                    if (nums[i] != tempMLeft) {
                        mLeft = i;
                        break;
                    }
                }
            } else {
                // 3.1.3 如果当前总和大于目标值，移动右指针,并且移动右指针后，如果右指针和右指针的上一个数相等，继续移动右指针
                // 3.1.3.1 保存当前中间右指针的值
                int tempMRight = nums[mRight];
                // 3.1.3.2 循环移动中间右指针
                for (int i = --mRight; i > 0; i--) {
                    if (nums[i] != tempMRight) {
                        mRight = i;
                        break;
                    }
                }
            }
        }
        return list;
    }
}
