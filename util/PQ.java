package cs2030.util;

import cs2030.util.Pair;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PQ<T> {

    private final PriorityQueue<T> priorityQueue;

    public PQ() {
        this.priorityQueue = new PriorityQueue<T>();
    }

    public PQ(Comparator<? super T> cmp) {
        this.priorityQueue = new PriorityQueue<T>(cmp);
    }

    public PQ(PriorityQueue<T> priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    @Override
    public String toString() {
        return this.priorityQueue.toString();
    }

    public PQ<T> add(T e) {
        PriorityQueue<T> newPriorityQueue = new PriorityQueue<>(priorityQueue);
        newPriorityQueue.add(e);
        return new PQ<T>(newPriorityQueue);
    }

    public boolean isEmpty() {
        return this.priorityQueue.isEmpty();
    }

    public Pair<T, PQ<T>> poll() {
        PriorityQueue<T> newPriorityQueue = new PriorityQueue<>(priorityQueue);
        T value = newPriorityQueue.poll();
        return Pair.of(value, new PQ<>(newPriorityQueue));
    }

    public int size() {
        return this.priorityQueue.size();
    }
}
