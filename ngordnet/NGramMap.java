package ngordnet;

import java.util.HashMap;
import java.util.Collection;
import edu.princeton.cs.introcs.In;

public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    private HashMap<Integer, YearlyRecord> y2yr;
    private HashMap<String, TimeSeries<Integer>> w2ts;
    private TimeSeries<Long> y2t;

    public NGramMap(String wordsFilename, String countsFilename) {
        y2yr = new HashMap<Integer, YearlyRecord>();
        w2ts = new HashMap<String, TimeSeries<Integer>>();
        y2t = new TimeSeries<Long>();
        In wordsReader = new In(wordsFilename);
        In countsReader = new In(countsFilename);
        String line, word;
        String[] lineArr;
        int year, no;
        while (!wordsReader.isEmpty()) {
            line = wordsReader.readLine();
            lineArr = line.split("\\t");
            word = lineArr[0];
            year = Integer.parseInt(lineArr[1]);
            no = Integer.parseInt(lineArr[2]);
            if (!y2yr.containsKey(year)) {
                y2yr.put(year, new YearlyRecord());
            }
            y2yr.get(year).put(word, no);

            if (!w2ts.containsKey(word)) {
                w2ts.put(word, new TimeSeries<Integer>());
            }
            w2ts.get(word).put(year, no);

        }

        while (!countsReader.isEmpty()) {
            line = countsReader.readLine();
            lineArr = line.split(",");
            year = Integer.valueOf(lineArr[0]);
            Long totalWords = Long.valueOf(lineArr[1]);
            y2t.put(year, totalWords);
        }

    }

    /**
     * Returns the absolute count of WORD in the given YEAR. If the word did not
     * appear in the given year, return 0.
     */
    public int countInYear(String word, int year) {
        try {
            return y2yr.get(year).count(word);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /** Returns a defensive copy of the YearlyRecord of the year. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord yr = y2yr.get(year);
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        Collection<String> wordSet = yr.words();
        for (String word : wordSet) {
            int c = yr.count(word);
            hm.put(word, c);
        }
        return new YearlyRecord(hm);
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return y2t;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear,
            int endYear) {
        return new TimeSeries<Integer>(countHistory(word), startYear, endYear);
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        if (w2ts.get(word) != null) {
            return new TimeSeries<Integer>(w2ts.get(word));
        } else {
            return null;
        }
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear,
            int endYear) {
        return new TimeSeries<Double>(weightHistory(word), startYear, endYear);
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Integer> h = countHistory(word);
        return h.dividedBy(y2t);
    }

    /**
     * Provides the summed relative frequency of all WORDS between STARTYEAR and
     * ENDYEAR.
     */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words,
            int startYear, int endYear) {
        return new TimeSeries<Double>(summedWeightHistory(words), startYear,
                endYear);
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        /* sum.put(0,0.0); */
        for (String word : words) {
            if (countHistory(word) != null) {
                if (sum.size() == 0) {
                    sum = weightHistory(word);
                } else {
                    sum = sum.plus(weightHistory(word));
                }
            }
        }
        return sum;
    }

    /**
     * Provides processed history of all words between STARTYEAR and ENDYEAR as
     * processed by YRP.
     */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
            YearlyRecordProcessor yrp) {
        return new TimeSeries<Double>(processedHistory(yrp), startYear, endYear);
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> pts = new TimeSeries<Double>();
        YearlyRecord yr;
        for (Integer y : y2yr.keySet()) {
            yr = getRecord(y);
            pts.put(y, yrp.process(yr));
        }
        return pts;
    }

}
