/** Provides examples of using the YearlyRecord class.
 *  @author Josh Hug
 */
package demos;
import ngordnet.YearlyRecord;
import java.util.Collection;
import java.util.HashMap;

public class YearlyRecordDemo {
    public static void main(String[] args) {
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);        
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);      
        System.out.println(yr.rank("surrogate")); // should print 1
        System.out.println(yr.rank("quayside")); // should print 3
        System.out.println(yr.size()); // should print 3

        Collection<String> words = yr.words();

        /* The code below should print: 
            
            quayside appeared 95 times.
            merchantman appeared 181 times.
            surrogate appeared 340 times.
        */
        for (String word : words) {
            System.out.println(word + " appeared " + yr.count(word) + " times.");
        }

        /* The code below should print the counts in ascending order:
            
            95
            181
            340
        */

        Collection<Number> counts = yr.counts();
        for (Number count : counts) {
            System.out.println(count);
        }

        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);
        yr2.put("auscultating", 1290);
        System.out.println(yr2.rank("berry"));
        System.out.println(yr2.rank("auscultating")); // should print 4

        YearlyRecord yr5 = new YearlyRecord();
        yr5.put("abc", 200);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 1
        System.out.println();
        yr5.put("abc", 1320);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 1
        System.out.println();
        yr5.put("def", 790);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 1
        System.out.println("Rank of def is " + yr5.rank("def")); // 2
        System.out.println();
        yr5.put("abc", 0);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 2
        System.out.println("Rank of def is " + yr5.rank("def")); // 1
        System.out.println();
        yr5.put("ghi", 600);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 3
        System.out.println("Rank of def is " + yr5.rank("def")); // 1
        System.out.println("Rank of ghi is " + yr5.rank("ghi")); // 2
        System.out.println();
        yr5.put("def", 1200);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 3
        System.out.println("Rank of def is " + yr5.rank("def")); // 1
        System.out.println("Rank of ghi is " + yr5.rank("ghi")); // 2
        System.out.println();
        yr5.put("jkl", 600);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 4
        System.out.println("Rank of def is " + yr5.rank("def")); // 1
        System.out.println("Rank of ghi is " + yr5.rank("ghi")); // 3 or 2
        System.out.println("Rank of jkl is " + yr5.rank("jkl")); // 2 or 3
        System.out.println();
        yr5.put("mno", 600);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 5
        System.out.println("Rank of def is " + yr5.rank("def")); // 1
        System.out.println("Rank of ghi is " + yr5.rank("ghi")); // 2 or 3 or 4
        System.out.println("Rank of jkl is " + yr5.rank("jkl")); // 2 or 3 or 4 (whatever hasn't been used above)
        System.out.println("Rank of mno is " + yr5.rank("mno")); // 2 or 3 or 4 (whatever hasn't been used above)
        System.out.println();
        yr5.put("pqr", 1200);
        System.out.println("Rank of abc is " + yr5.rank("abc")); // 6
        System.out.println("Rank of def is " + yr5.rank("def")); // 1 or 2
        System.out.println("Rank of ghi is " + yr5.rank("ghi")); // 3 or 4 or 5
        System.out.println("Rank of jkl is " + yr5.rank("jkl")); // 3 or 4 or 5 (whatever hasn't been used above)
        System.out.println("Rank of mno is " + yr5.rank("mno")); // 3 or 4 or 5 (whatever hasn't been used above)
        System.out.println("Rank of pqr is " + yr5.rank("pqr")); // 1 or 2 (whatever hasn't been used for def's rank)
        System.out.println();
    }
} 
