package com.vehiclerental.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom singly-linked list used across domain services for in-memory data loading.
 */
public class LinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; this.next = null; }
    }

    private Node<T> head;
    private int size;

    public LinkedList() { this.head = null; this.size = 0; }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) { head = newNode; }
        else {
            Node<T> current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public java.util.List<T> toList() {
        java.util.List<T> result = new java.util.ArrayList<>();
        Node<T> current = head;
        while (current != null) { result.add(current.data); current = current.next; }
        return result;
    }

    public T get(int index) {
        Node<T> current = head;
        int count = 0;
        while (current != null) {
            if (count == index) return current.data;
            current = current.next;
            count++;
        }
        return null;
    }

    public void delete(int index) {
        if (head == null) return;
        if (index == 0) { head = head.next; size--; return; }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            if (current.next == null) return;
            current = current.next;
        }
        if (current.next != null) { current.next = current.next.next; size--; }
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}
