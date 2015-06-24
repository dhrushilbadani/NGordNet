package ngordnet;

import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {
    @Override
    public double process(YearlyRecord yearlyRecord) {
        Collection<String> wordset = yearlyRecord.words();
        int currCount = 0, wordLength = 0;
        double avg, totalLength = 0.0, totalCount = 0.0;
        for (String word : wordset) {
            currCount = yearlyRecord.count(word);
            wordLength = word.length();
            totalLength = totalLength + currCount * wordLength;
            totalCount = totalCount + currCount;
        }
        avg = totalLength / totalCount;
        return avg;
    }
}
