package model;

import java.util.Comparator;

public class ChessPair< K extends Comparable<K>, V>
        implements Comparable<ChessPair<K, V>> {
    private K key;
    private V value;

    public ChessPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String print() {
        return key.toString() + " -> " + value.toString();
    }

    @Override
    public int compareTo(ChessPair<K, V> obj) {
        return new ChessPairComparator<K, V>().compare(this, obj);
    }
}
