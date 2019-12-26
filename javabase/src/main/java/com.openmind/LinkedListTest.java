package com.openmind;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * jishuzhan
 *
 * offer和add的相同，offer调用了add方法，push调用了addFirst，offerFirst调用了addFirst
 * peek、poll、remove都可以获取表头数据，但是peek不会删除，poll和remove则会删除该元素并返回
 * poll和remove底层都调用了unlinkFirst,但是当节点为null时，poll返回null，remove会报NoSuchElementException异常
 *
 * @author zhoujunwen
 * @date 2019-12-23
 * @time 10:24
 * @desc
 */
public class LinkedListTest {

    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList();
        linkedList.add("pig");
        linkedList.add("test");
        linkedList.offer("bird");
        linkedList.offer("sheep");
        linkedList.offerFirst("zoo");
        linkedList.push("cat");
        linkedList.offerLast("horse");
        linkedList.push("dog");
        linkedList.offerFirst("xiaorenwu");

        System.out.println(linkedList.get(0));
        System.out.println(linkedList.getFirst());
        System.out.println(linkedList.getLast());
        System.out.println(linkedList.peek());
//        System.out.println(linkedList.poll());
//        System.out.println(linkedList.pollLast());
        System.out.println(linkedList.element());
        System.out.println(linkedList);
//        System.out.println(linkedList.remove());

//        System.out.println(Collections.binarySearch(linkedList,"dog"));
//        Collections.sort(linkedList);
//        System.out.println(linkedList);
//        System.out.println(Collections.binarySearch(linkedList,"dog"));
//        Collections.reverse(linkedList);
//        System.out.println(linkedList);

        System.out.println(Collections.binarySearch(linkedList, "bird", Comparator.naturalOrder()));
    }
}
