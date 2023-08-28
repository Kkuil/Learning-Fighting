package com.kkuil.model;

/**
 * @Author Kkuil
 * @Date 2023/8/26 23:25
 * @Description 列表节点
 */
public class ListNode {
    int val;
    ListNode next;

    public ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
