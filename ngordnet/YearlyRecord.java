package ngordnet;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Set;

public class YearlyRecord {
    /** Creates a new empty YearlyRecord. */
    private TreeMap<Double, String> count2word;
    private HashMap<String, Double> word2count;
    private HashMap<String, Integer> word2rank;
    private TreeSet<Number> counts;
    private static final double TIEBREAKER = 0.00001;
    private int k = (int) Math.sqrt(1);
    private boolean frozen;
    private int size;
    private double xDouble;

    public YearlyRecord() {
        count2word = new TreeMap<Double, String>();
        word2count = new HashMap<String, Double>();
        counts = new TreeSet<Number>();
        word2rank = new HashMap<String, Integer>();
        size = 0;
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        this();
        size = otherCountMap.size();
        Set<String> stringSet = otherCountMap.keySet();
        for (String K : stringSet) {
            Integer X = otherCountMap.get(K);
            xDouble = X + 0.0;
            if (count2word.containsKey(xDouble)) {
                xDouble = xDouble + (k * TIEBREAKER);
                k = k + 1;
            }
            if (word2count.containsKey(K)) {
                count2word.remove(word2count.get(K));
                size = size - 1;
            }
            count2word.put(xDouble, K);
            word2count.put(K, xDouble);
            counts.add(X);
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        try {
            return (int) word2count.get(word).doubleValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        size += 1;
        xDouble = count + 0.0;
        if (count2word.containsKey(xDouble)) {
            xDouble = xDouble + (k * TIEBREAKER);
            k = k + 1;
        }
        if (word2count.containsKey(word)) {
            count2word.remove(word2count.get(word));
            size = size - 1;
        }
        count2word.put(xDouble, word);
        word2count.put(word, xDouble);
        counts.add(count);
        frozen = false;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return size;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        return count2word.values();
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        return counts;
    }

    /** Returns rank of WORD. Most common word is rank 1. */
    public int rank(String word) {
        /*
         * Integer count = word2count.get(word); int rank =
         * size-(count2word.headMap(count)).size(); return rank;
         */

        if (frozen) {
            return word2rank.get(word);
        } else {
            int returnval = 0, rank = size;
            for (Double c : count2word.keySet()) {
                String w = count2word.get(c);
                /* int rank = size-(count2word.headMap(count)).size(); */
                word2rank.put(w, rank);
                if (word.equals(w)) {
                    returnval = rank;
                }
                rank = rank - 1;
            }
            frozen = true;
            return returnval;
        }

    }
}
