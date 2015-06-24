package ngordnet;

import java.util.Set;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    /*
     * private HashSet<Number> yrs =new HashSet<Number>(); private
     * HashSet<Number> data=new HashSet<Number>();
     */
    public TimeSeries() {
        super();
    }

    /**
     * Returns the years in which this time series is valid. Doesn't really need
     * to be a NavigableSet. This is a private method and you don't have to
     * implement it if you don't want to.
     */
    /*
     * private NavigableSet<Integer> validYears(int startYear, int endYear) {
     *
     * }
     */

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR. inclusive
     * of both end points.
     */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        this();
        for (Integer K : ts.keySet()) {
            if (K >= startYear && K <= endYear) {
                this.put(K, ts.get(K));
            }
        }

    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        this(ts, ts.firstKey(), ts.lastKey());
    }

    /**
     * Returns the quotient of this time series divided by the relevant value in
     * ts. If ts is missing a key in this time series, return an
     * IllegalArgumentException.
     */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> q = new TimeSeries<Double>();
        double val;
        for (Integer K : this.keySet()) {
            if (!ts.keySet().contains(K)) {
                throw new IllegalArgumentException("Year mismatch");
            }
            val = this.get(K).doubleValue() / ts.get(K).doubleValue();
            q.put(K, new Double(val));
        }
        return q;
    }

    /**
     * Returns the sum of this time series with the given ts. The result is a a
     * Double time series (for simplicity).
     */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {

        TimeSeries<Double> sum = new TimeSeries<Double>();
        Set<Integer> tsSet = ts.keySet();
        Set<Integer> thisSet = this.keySet();
        double s = 0;
        for (Integer K : tsSet) {
            s = ts.get(K).doubleValue();
            if (thisSet.contains(K)) {
                s += this.get(K).doubleValue();
            }
            sum.put(K, new Double(s));
        }

        for (Integer K : thisSet) {
            s = this.get(K).doubleValue();
            if (!tsSet.contains(K)) {
                sum.put(K, new Double(s));
            }
        }

        return sum;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        ArrayList<Number> years = new ArrayList<Number>(this.keySet());
        return years;
    }

    /** Returns all data for this time series (in any order). */
    public Collection<Number> data() {
        ArrayList<Number> data = new ArrayList<Number>(this.values());
        return data;
    }
}
