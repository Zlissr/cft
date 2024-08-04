package ru.raytrace;

import java.util.Iterator;
import java.util.List;

public class MultiIterator<T> implements Iterator<T> {

    private final List<Iterator<T>> iterators;
    private int index = 0;

    public MultiIterator(List<Iterator<T>> iterators) {
        this.iterators = iterators;
    }

    @Override
    public boolean hasNext() {
        return iterators.stream().anyMatch(Iterator::hasNext);
    }

    @Override
    public T next() {
        while (hasNext()) {
            if (index >= iterators.size()) {
                index = 0;
            }
            if (iterators.get(index++).hasNext()) {
                return iterators.get(index - 1).next();
            }
        }
        return null;
    }
}
