package com.my.experiment.demo.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map + 链表实现LRU缓存
 * @param <K>
 * @param <V>
 */
public class LRUCacheUtil<K,V> {

    class Node<K,V>{
        K key;
        V value;
        Node<K,V> prev;
        Node<K,V> next;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    class DoubleLinkedList<K,V>{
        Node<K,V> head;
        Node<K,V> tail;

        public DoubleLinkedList() {
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        public void addFirstNode(Node<K,V> node){
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }

        public void removeCurrentNode(Node<K,V> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }

        public Node<K,V> getLastNode(){
            return tail.prev;
        }
    }

    private Map<K,Node<K,V>> cacheMap;

    private DoubleLinkedList<K,V> linkedList;

    private int maxSize;

    public LRUCacheUtil(Map<K, Node<K, V>> cacheMap, int maxSize) {
        this.cacheMap = cacheMap;
        this.linkedList = new DoubleLinkedList<>();
        this.maxSize = maxSize;
    }

    public V get(K key){
        if(!cacheMap.containsKey(key)){
            return null;
        }
        Node<K, V> node = cacheMap.get(key);
        linkedList.removeCurrentNode(node);
        linkedList.addFirstNode(node);
        return node.value;
    }

    public void put(K key,V value){
        if(cacheMap.containsKey(key)){
            Node<K, V> node = cacheMap.get(key);
            node.value = value;
            cacheMap.put(key,node);   //可有可无，引用同一个node
            linkedList.removeCurrentNode(node);
            linkedList.addFirstNode(node);
        }else {

            if(cacheMap.size() >= maxSize ){
                Node<K, V> lastNode = linkedList.getLastNode();
                cacheMap.remove(lastNode.key);
                linkedList.removeCurrentNode(lastNode);
            }
            Node<K, V> newNode = new Node<>(key, value);
            cacheMap.put(key,newNode);
            linkedList.addFirstNode(newNode);
        }
    }

    public static void main(String[] args) {
        LRUCacheUtil<Integer, Integer> lruUtil = new LRUCacheUtil<>(new ConcurrentHashMap<>(),3);
        lruUtil.put(1,1);
        lruUtil.put(2,2);
        lruUtil.put(3,3);
        System.out.println(lruUtil.cacheMap.keySet());
        lruUtil.put(4,4);
        System.out.println(lruUtil.cacheMap.keySet());
        lruUtil.get(2);
        System.out.println(lruUtil.cacheMap.keySet());
        lruUtil.put(5,5);
        System.out.println(lruUtil.cacheMap.keySet());
        lruUtil.put(5,6);
        System.out.println(lruUtil.get(5));
        lruUtil.put(3,31);
        System.out.println("put 3 ,value:" + lruUtil.get(3));
        lruUtil.put(3,32);
        System.out.println("更新 3 后,value:" + lruUtil.get(3));
    }
}
