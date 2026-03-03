package model;
import java.util.Comparator;

public class ChessPairComparator< K extends Comparable<K>, V>
    implements Comparator<ChessPair<K, V>> {
    @Override
    public int compare(ChessPair<K,V> a, ChessPair<K,V> b) {
        return a.getKey().compareTo(b.getKey());
    }
}
