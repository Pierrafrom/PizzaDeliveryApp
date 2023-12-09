package com.pizzadelivery.utils;

/**
 * A custom generic linked list implementation with a maximum size limit.
 *
 * @param <Type> The type of elements stored in the list.
 */
public class List<Type> {
    private Type head;
    private List<Type> rest;
    private int size;
    private final int maxSize;

    /**
     * Constructs a new list with a given element as its head, a reference to another list as its tail,
     * and a maximum size limit.
     *
     * @param e       The element to set as the head of the list.
     * @param l       The reference to the rest of the list.
     * @param maxSize The maximum size limit for the list.
     */
    public List(Type e, List<Type> l, int maxSize) {
        this.head = e;
        this.rest = l;
        this.size = 1;
        this.maxSize = maxSize;
    }

    /**
     * Constructs a new list with a given element as its head, a reference to another list as its tail,
     * and no maximum size limit (default to Integer.MAX_VALUE).
     *
     * @param e The element to set as the head of the list.
     * @param l The reference to the rest of the list.
     */
    public List(Type e, List<Type> l) {
        this(e, l, Integer.MAX_VALUE);
    }

    /**
     * Returns the rest of the list.
     *
     * @return The rest of the list.
     * @throws NullPointerException if the rest of the list is empty (null).
     */
    public List<Type> getRest() throws NullPointerException {
        if (this.rest == null) {
            throw new NullPointerException("Rest of the list is empty.");
        }
        return this.rest;
    }

    /**
     * Returns the head of the list.
     *
     * @return The head of the list.
     * @throws NullPointerException if the head of the list is empty (null).
     */
    public Type getHead() throws NullPointerException {
        if (this.head == null) {
            throw new NullPointerException("Head of the list is empty.");
        }
        return this.head;
    }

    /**
     * Sets the rest of the list.
     *
     * @param l The list to set as the rest of the current list.
     */
    public void setRest(List<Type> l) {
        this.rest = l;
    }

    /**
     * Sets the head of the list.
     *
     * @param t The element to set as the head of the list.
     */
    public void setHead(Type t) {
        this.head = t;
    }

    /**
     * Returns the length of the list.
     *
     * @return The length of the list.
     */
    public int length() {
        return this.size;
    }

    /**
     * Returns the maximum size limit of the list.
     *
     * @return The maximum size limit of the list.
     */
    public int maxSize() {
        return this.maxSize;
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public Type get(int index) {
        if (index < 0 || index >= this.length()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return getElementAtIndex(index);
    }

    private Type getElementAtIndex(int index) {
        if (index == 0) {
            return this.getHead();
        } else {
            return this.getRest().getElementAtIndex(index - 1);
        }
    }

    /**
     * Removes the element at the specified index from the list.
     *
     * @param index The index of the element to remove.
     */
    public void remove(int index) {
        if (index == 0) {
            this.setHead(this.getRest().getHead());
            this.setRest(this.getRest().getRest());
        } else {
            this.getRest().remove(index - 1);
        }
        this.size--;
    }

    /**
     * Inserts an element at the specified index in the list.
     *
     * @param index The index at which to insert the element.
     * @param e     The element to insert.
     */
    public void add(int index, Type e) {
        if (this.length() < this.maxSize()) {
            if (index == 0) {
                List<Type> newRest = new List<>(this.getHead(), this.getRest());
                this.setHead(e);
                this.setRest(newRest);
            } else {
                this.getRest().add(index - 1, e);
            }
            this.size++;
        } else {
            throw new IllegalStateException("List size exceeds the maximum limit.");
        }
    }

    /**
     * Checks if the list contains a specific element.
     *
     * @param e The element to check for in the list.
     * @return True if the element is found in the list, false otherwise.
     */
    public boolean contains(Type e) {
        if (this.getHead().equals(e)) {
            return true;
        } else {
            try {
                return this.getRest().contains(e);
            } catch (NullPointerException ignored) {
                return false;
            }
        }
    }
}
