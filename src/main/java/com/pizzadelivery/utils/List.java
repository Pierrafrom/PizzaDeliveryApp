package com.pizzadelivery.utils;

/**
 * A custom generic linked list implementation with a maximum size limit.
 *
 * @param <Type> The type of elements stored in the list.
 */
public class List<Type> {
    private Type head;
    private List<Type> rest;
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

    public List() {
        this(null, null, Integer.MAX_VALUE);
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
        try {
            getHead();
        } catch (NullPointerException e) {
            return 0;
        }
        try {
            return 1 + this.getRest().length();
        } catch (NullPointerException e) {
            return 1;
        }
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

    public void set(int index, Type element) {
        if (index == 0) {
            this.setHead(element);
        } else {
            this.getRest().set(index - 1, element);
        }
    }

    /**
     * Returns a string representation of the list.
     * Each element of the list is represented in the format 'head={headValue}, rest={restValue}'.
     *
     * @return A string representation of the list.
     */
    public String toString() {
        return "List{" +
                "head=" + head + ", " +
                "rest=" + rest +
                "}";
    }

    /**
     * Removes the element at the specified index from the list.
     * If the index is 0, it removes the head of the list and sets the next element as the new head.
     * For other indices, it traverses the list to find the element at that index and removes it.
     *
     * @param index The index of the element to remove.
     * @return The element that has been removed.
     * @throws IndexOutOfBoundsException if the index is out of bounds of the list size.
     */
    public Type remove(int index) {
        if (index < 0 || index >= this.length()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Type removedElement = null;

        if (index == 0) {
            try {
                removedElement = this.getHead();
                this.setHead(this.getRest().getHead());
                this.setRest(this.getRest().getRest());
            } catch (NullPointerException ex) {
                this.setHead(null);
                this.setRest(null);
            }
        } else {
            List<Type> current = this;
            for (int i = 0; i < index - 1; i++) {
                current = current.getRest();
            }
            try {
                removedElement = current.getRest().getHead();
                current.setRest(current.getRest().getRest());
            } catch (NullPointerException ex) {
                current.setRest(null);
            }
        }

        return removedElement;
    }

    /**
     * Removes the specified element from the list.
     * If the element to remove is the head, it sets the next element as the new head.
     * If not, it searches for the element in the list and removes it.
     *
     * @param e The element to remove from the list.
     * @return The element that has been removed.
     * @throws NullPointerException if the list is empty or the element is not found.
     */
    public Type remove(Type e) {
        Type removedElement = null;

        try {
            if (this.getHead().equals(e)) {
                removedElement = this.getHead();
                this.setHead(this.getRest().getHead());
                this.setRest(this.getRest().getRest());
            } else {
                removedElement = removeElementRecursively(this, e);
            }
        } catch (NullPointerException ex) {
            this.setHead(null);
            this.setRest(null);
        }

        return removedElement;
    }

    /**
     * Helper method to remove a specified element from the list recursively.
     * This method is called by the public remove(Type e) method and should not be called directly.
     *
     * @param current The current list node being examined.
     * @param e       The element to remove from the list.
     * @return The element that has been removed.
     * @throws NullPointerException if the element is not found in the list.
     */
    private Type removeElementRecursively(List<Type> current, Type e) {
        Type removedElement = null;

        try {
            if (current.getRest().getHead().equals(e)) {
                removedElement = current.getRest().getHead();
                current.setRest(current.getRest().getRest());
            } else {
                removedElement = removeElementRecursively(current.getRest(), e);
            }
        } catch (NullPointerException ex) {
            current.setRest(null);
        }

        return removedElement;
    }

    /**
     * Inserts an element at the specified index in the list.
     *
     * @param index The index at which to insert the element.
     * @param e     The element to insert.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     * @throws IllegalStateException     if the list size exceeds the maximum limit.
     */
    public void add(int index, Type e) {
        if (this.length() <= this.maxSize()) {
            try {
                if (index == 0) {
                    // Creating a new rest of the list and updating the head
                    List<Type> newRest = new List<>(this.getHead(), this.getRest(), this.maxSize());
                    this.setHead(e);
                    this.setRest(newRest);
                } else {
                    // Recursive call to add at the correct index if rest is not null
                    this.getRest().add(index - 1, e);
                }
            } catch (NullPointerException ex) {
                if (index == 0) {
                    // If the rest is null and index is 0, simply add the element at the head
                    this.setHead(e);
                    this.setRest(new List<>(null, null, this.maxSize()));
                } else if (index == 1) {
                    // If the rest is null but index is 1, add the element as the new rest
                    this.setRest(new List<>(e, null, this.maxSize()));
                } else {
                    // If index is invalid (greater than 1 and the rest is null), throw an IndexOutOfBoundsException
                    throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for list of size " + this.length());
                }
            }
        } else {
            throw new IllegalStateException("List size exceeds the maximum limit.");
        }
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param e The element to add to the list.
     */
    public void add(Type e) {
        try {
            this.getHead();
        } catch (NullPointerException ex) {
            this.setHead(e);
            return;
        }
        this.add(this.length(), e);
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

    /**
     * Checks if the list is empty.
     *
     * @return True if the list is empty, false otherwise.
     */
    public boolean empty() {
        return this.length() == 0;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        this.setHead(null);
        this.setRest(null);
    }

    /**
     * Adds all elements from a list to the current list.
     *
     * @param list The list to add to the current list.
     */
    public void addAll(List<Type> list) {
        for (int i = 0; i < list.length(); i++) {
            this.add(list.get(i));
        }
    }

    /**
     * Returns a clone of the current list.
     *
     * @return A clone of the current list.
     */
    public List<Type> clone() {
        List<Type> clone = new List<>(null, null, this.maxSize());
        clone.addAll(this);
        return clone;
    }

    public void generatePermutations(int index, List<List<Type>> permutations) {
        if (index == this.length() - 1) {
            permutations.add(this.clone());
        } else {
            for (int i = index; i < this.length(); i++) {
                this.swap(index, i);
                this.generatePermutations(index + 1, permutations);
                this.swap(index, i); // Swap back for backtracking
            }
        }
    }

    private void swap(int indexOne, int indexTwo) {
        Type temp = this.get(indexOne);
        this.set(indexOne, this.get(indexTwo));
        this.set(indexTwo, temp);
    }

    public int indexOf(Type order) {
        if (this.getHead().equals(order)) {
            return 0;
        } else {
            try {
                return 1 + this.getRest().indexOf(order);
            } catch (NullPointerException ex) {
                return -1;
            }
        }
    }
}
