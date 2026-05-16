package com.vehiclerental.linkedlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Generic Singly Linked List
 * Used as the core data structure across all components of the
 * Vehicle Rental Service Platform (SE1020 OOP Assignment)
 *
 * Operations: addFirst, addLast, delete, search, display, size
 */
public class LinkedList<T> {

    // Inner Node class
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    // ─── Insert Operations ──────────────────────────────────────────────────

    /** Insert a new node at the beginning of the list */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    /** Insert a new node at the end of the list */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // ─── Delete Operation ───────────────────────────────────────────────────

    /** Remove the first node matching the given data (by equals) */
    public boolean delete(T data) {
        if (head == null) return false;

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Remove a node by index */
    public boolean deleteByIndex(int index) {
        if (index < 0 || index >= size) return false;
        if (index == 0) {
            head = head.next;
            size--;
            return true;
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        current.next = current.next.next;
        size--;
        return true;
    }

    // ─── Search & Access ────────────────────────────────────────────────────

    /** Get element at given index */
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /** Update data at given index */
    public boolean set(int index, T data) {
        if (index < 0 || index >= size) return false;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.data = data;
        return true;
    }

    /** Convert linked list to Java List for easy iteration */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    /** Display all nodes (traversal) */
    public void display() {
        Node<T> current = head;
        System.out.print("LinkedList: ");
        while (current != null) {
            System.out.print("[" + current.data + "] -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void clear() { head = null; size = 0; }
}
