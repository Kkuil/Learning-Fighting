package com.kkuil;

/**
 * @Author Kkuil
 * @Date 2023/8/13 17:29
 * @Description 字符串相加
 */
public class AddStrings {

    public static void main(String[] args) {
        System.out.println("addStrings(\"123\", \"456\") = " + addStrings("123", "456"));
    }

    /**
     * @param num1 数字1
     * @param num2 数字2
     * @return 相加结果
     */
    public static String addStrings(String num1, String num2) {
        // 1. 定义结果字符串
        StringBuilder result = new StringBuilder();
        // 2. 定义进位
        int carry = 0;
        // 3. 定义两个指针，分别指向两个字符串的末尾
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        // 4. 遍历两个字符串
        while (i >= 0 || j >= 0) {
            // 4.1 计算当前位的值
            int n1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int n2 = j >= 0 ? num2.charAt(j) - '0' : 0;
            // 4.2 计算当前位的和
            int sum = n1 + n2 + carry;
            // 4.3 计算当前位的进位
            carry = sum / 10;
            // 4.4 计算当前位的值
            result.append(sum % 10);
            // 4.5 移动指针
            i--;
            j--;
        }
        // 5. 如果进位不为0，添加到结果字符串中
        if (carry == 1) {
            result.append(1);
        }
        // 6. 反转结果字符串
        return result.reverse().toString();
    }
}
