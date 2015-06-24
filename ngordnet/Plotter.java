package ngordnet;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.ChartBuilder;

public class Plotter {
    /**
     * Creates a plot of the TimeSeries TS. Labels the graph with the given
     * TITLE, XLABEL, YLABEL, and LEGEND.
     */
    private static final int EIGHT = 800;
    private static final int SIX = 600;
    public static void plotTS(TimeSeries<? extends Number> ts, String title,
            String xlabel, String ylabel, String legend) {
        Collection<Number> xValues = ts.years();
        Collection<Number> yValues = ts.data();
        Chart chart = QuickChart.getChart(title, ylabel, xlabel, legend,
                xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    /**
     * Creates a plot of the absolute word counts for WORD from STARTYEAR to
     * ENDYEAR, using NGM as a data source.
     */
    public static void plotCountHistory(NGramMap ngm, String word,
            int startYear, int endYear) {
        TimeSeries<Integer> hist = ngm.countHistory(word, startYear, endYear);
        plotTS(hist, "Absolute count of " + word + " vs. Year", "Year",
                "Absolute count", word);
    }

    /**
     * Creates a plot of the normalized weight counts for WORD from STARTYEAR to
     * ENDYEAR, using NGM as a data source.
     */
    public static void plotWeightHistory(NGramMap ngm, String word,
            int startYear, int endYear) {
        TimeSeries<Double> wtHist = ngm
                .weightHistory(word, startYear, endYear);
        plotTS(wtHist, "Normalized Weight Count of " + word + " vs. Year",
                "Year", "Normalized Weight Count", word);
    }

    /**
     * Creates a plot of the processed history from STARTYEAR to ENDYEAR, using
     * NGM as a data source, and the YRP as a yearly record processor.
     */
    public static void plotProcessedHistory(NGramMap ngm, int startYear,
            int endYear, YearlyRecordProcessor yrp) {
        TimeSeries<Double> phist = ngm
                .processedHistory(startYear, endYear, yrp);
        plotTS(phist, "Length of Average Word vs. Year", "Year",
                "Length of Average Word", "Length of Average Word");
    }

    /**
     * Creates a plot of the total normalized count of every word that is a
     * hyponym of CATEGORYLABEL from STARTYEAR to ENDYEAR using NGM and WN as
     * data sources.
     */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn,
            String categoryLabel, int startYear, int endYear) {
        Set<String> hyps = wn.hyponyms(categoryLabel);
        TimeSeries<Double> hist = ngm.summedWeightHistory(hyps, startYear,
                endYear);
        plotTS(hist, "Total Normalized Weight Count of Hyponyms of "
                + categoryLabel + " vs. Year", "Year", categoryLabel
                + "\\'s Hyponyms\\' Total Normalized Weight", categoryLabel);
    }

    /**
     * Creates overlaid category weight plots for each category label in
     * CATEGORYLABELS from STARTYEAR to ENDYEAR using NGM and WN as data
     * sources.
     */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn,
            String[] categoryLabels, int startYear, int endYear) {
        Chart chart = new ChartBuilder().width(EIGHT).height(SIX)
                .xAxisTitle("Years")
                .yAxisTitle("Hyponyms' Total Normalized Weight").build();
        for (String categoryLabel : categoryLabels) {
            Set<String> hyps = wn.hyponyms(categoryLabel);
            TimeSeries<Double> hist = ngm.summedWeightHistory(hyps, startYear,
                    endYear);
            Collection<Number> currXValues = hist.years();
            Collection<Number> currYValues = hist.data();
            chart.addSeries(categoryLabel, currXValues, currYValues);
        }
        new SwingWrapper(chart).displayChart();
    }

    /**
     * Makes a plot showing overlaid individual normalized count for every word
     * in WORDS from STARTYEAR to ENDYEAR using NGM as a data source.
     */
    public static void plotAllWords(NGramMap ngm, String[] words,
            int startYear, int endYear) {
        Chart chart = new ChartBuilder().width(EIGHT).height(SIX)
                .xAxisTitle("Years").yAxisTitle("Normalized Count").build();
        for (String word : words) {
            TimeSeries<Double> hist = ngm.weightHistory(word, startYear,
                    endYear);
            Collection<Number> currXValues = hist.years();
            Collection<Number> currYValues = hist.data();
            chart.addSeries(word, currXValues, currYValues);
        }
        new SwingWrapper(chart).displayChart();
    }

    /** Returns the numbers from max to 1, inclusive in decreasing order. */
    private static Collection downRange(int max) {
        ArrayList rv = new ArrayList();
        for (int i = max; i >= 1; i -= 1) {
            rv.add(i);
        }
        return rv;
    }

    /**
     * Plots the normalized count of every word against the rank of every word
     * on a log-log plot. Uses data from YEAR, using NGM as a data source.
     */
    public static void plotZipfsLaw(NGramMap ngm, int year) {
        YearlyRecord yr = ngm.getRecord(year);
        Collection yrCounts = yr.counts();
        Collection yrRanks = downRange(yrCounts.size());

        Chart chart = new ChartBuilder().width(EIGHT).height(SIX)
                .xAxisTitle("Rank").yAxisTitle("Count").build();
        chart.getStyleManager().setYAxisLogarithmic(true);
        chart.getStyleManager().setXAxisLogarithmic(true);

        chart.addSeries("Zipf's Law", yrRanks, yrCounts);
        new SwingWrapper(chart).displayChart();
    }

}
