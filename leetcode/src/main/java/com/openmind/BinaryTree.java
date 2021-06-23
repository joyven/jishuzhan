package com.openmind;

import java.util.Arrays;
import java.util.Objects;

/**
 * BinaryTree
 *
 * @author zhoujunwen
 * @date 2021-06-22 14:02
 * @desc
 */
public class BinaryTree {
    public static void main(String[] args) {
        BinaryTree t = new BinaryTree();
        int[] pre = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = {4, 7, 2, 1, 5, 3, 8, 6};
        int[] next = {7, 4, 2, 5, 8, 6, 3, 1};

        TreeNode tn = t.reConstructBinaryTree(pre, in);
        TreeNode tn2 = t.reConstructBinaryTree2(next, in);
        System.out.println(tn);
        System.out.println(tn2);
        System.out.println(tn.equals(tn2));
    }


    public static class TreeNode {
        int data;
        TreeNode left;
        TreeNode right;

        TreeNode(int data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "TreeNode{" +
                    "data=" + data +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            TreeNode treeNode = (TreeNode) o;
            return data == treeNode.data;
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    /**
     * 通过前序和中序构建二叉树
     *
     * @param pre 前序遍历序列
     * @param in  中序遍历序列
     * @return
     */
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        if (pre == null || in == null) {
            return null;
        }
        if (pre.length == 0 || in.length == 0) {
            return null;
        }
        if (pre.length != in.length) {
            return null;
        }
        TreeNode root = new TreeNode(pre[0]);
        for (int i = 0; i < pre.length; i++) {
            // 根据根节点元素在中序遍历中找到左子树和右子树的节点
            if (pre[0] == in[i]) {
                root.left = reConstructBinaryTree(Arrays.copyOfRange(pre, 1, i + 1), Arrays.copyOfRange(in, 0, i));
                root.right = reConstructBinaryTree(Arrays.copyOfRange(pre, i + 1, pre.length), Arrays.copyOfRange(in, i + 1, in.length));
            }
        }
        return root;
    }

    /**
     * 通过后序和中序构建二叉树
     *
     * @param next 后序遍历序列(左右根)
     * @param in   中序遍历序列(左根右)
     * @return
     */
    public TreeNode reConstructBinaryTree2(int[] next, int[] in) {
        if (next == null || in == null) {
            return null;
        }
        if (next.length == 0 || in.length == 0) {
            return null;
        }
        if (next.length != in.length) {
            return null;
        }

        TreeNode root = new TreeNode(next[next.length - 1]);
        for (int i = next.length - 1; i >= 0; i--) {
            if (next[next.length - 1] == in[i]) {
                root.left = reConstructBinaryTree2(Arrays.copyOfRange(next, 0, i), Arrays.copyOfRange(in, 0, i));
                root.right = reConstructBinaryTree2(Arrays.copyOfRange(next, i, next.length - 1), Arrays.copyOfRange(in, i + 1, in.length));
            }
        }
        return root;
    }
}
