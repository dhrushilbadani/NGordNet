package ngordnet;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/**
 * Provides a simple user interface for exploring WordNet and NGram data.
 *
 * @author Dhrushil Badani
 */
public class NgordnetUI {

    private static final int SD = 1505;
    private static final int ED = 2008;    
    /* private static void wordlength(NGramMap ng, int start, int end) {
        YearlyRecord rec;
        WordLengthProcessor wrp = new WordLengthProcessor();
        TimeSeries<Double> ts = new TimeSeries<Double>();
        double val;
        for (int y = start; y <= end; y++) {
            rec = ng.getRecord(y);
            val = wrp.process(rec);
            ts.put(y, val);
        }
        Plotter.plotTS(ts, "Average Wordlength vs. Year", "Year",
                "Avg. wordlength", "Wordlength");
    
    } **/

    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");
        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                + wordFile + ", " + countFile + ", " + synsetFile
                + ", and " + hyponymFile + ".");
        int startDate = SD, endDate = ED;
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        YearlyRecordProcessor yrp = new WordLengthProcessor();
        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            int l = tokens.length;
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            try {
                switch (command) {
                    case "quit":
                        return;
                    case "help":
                        String helpStr = new In("./ngordnet/help.txt").readAll();
                        System.out.println(helpStr);
                        break;
                    case "range":
                        if (startDate < endDate && l == 2) {
                            startDate = Integer.parseInt(tokens[0]);
                            endDate = Integer.parseInt(tokens[1]);
                            System.out.println("Start date:" + startDate + "\nEnd date:" + endDate);
                        }
                        break;
                    case "count":
                        if (l == 2) {
                            int year = Integer.parseInt(tokens[1]);
                            YearlyRecord yr = ngm.getRecord(year);
                            System.out.println(yr.count(tokens[0]) + "\n");
                        }
                        break;
                    case "hyponyms":
                        if (l == 1) {
                            System.out.println(wn.hyponyms(tokens[0]));
                        }
                        break;
                    case "history":
                        Plotter.plotAllWords(ngm, tokens, startDate, endDate);
                        break;
                    case "hypohist":
                        Plotter.plotCategoryWeights(ngm, wn, tokens, startDate,
                                endDate);
                        break;
                    case "zipf":
                        if (l == 1) {
                            Plotter.plotZipfsLaw(ngm,
                                    Integer.parseInt(tokens[0]));
                        }
                        break;
                    case "wordlength":
                        if (l == 0) {
                            Plotter.plotProcessedHistory(ngm, startDate, endDate, yrp);
                        }
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number.");
            } catch (NullPointerException npe) {
                System.out.println("Invalid input.");
            } catch (ArrayIndexOutOfBoundsException aiob) {
                System.out.println("Invalid input length");
            }
        }
    }
}
