import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CountingBloomFilter {

    static int bitArray[];
    static int numEntriesinitial;
    static int numEntriesRemoved;
    static int numEntriesAdded;

    static Set<Integer>
            uniqueValsInitial;

    static Set<Integer>
            uniqueValsToBeAdded;
    static Set<Integer> globalSet;

    static int hashes;
    static int[] randValue;
    static int bitArrayLen;

    public CountingBloomFilter(int entriesInitially, int entriesRemoved, int entriesAdded, int numBits, int numHashes) {
        numEntriesinitial = entriesInitially;
        numEntriesRemoved = entriesRemoved;
        numEntriesAdded = entriesAdded;
        bitArray = new int[numBits];
        globalSet = new HashSet<>();
        bitArrayLen = numBits;
        hashes = numHashes;
        uniqueValsInitial = new HashSet<>();
        uniqueValsToBeAdded = new HashSet<>();

        randValue = new int[numHashes];
        Random rd = new Random();

        for (int i = 0; i < numHashes; i++) {
            randValue[i] = 0 + rd.nextInt(Integer.MAX_VALUE);// min + rd.nextInt(maxValue)
        }
    }

    public static void callCountingBloomFilter(int totInitialEntries, int entryRemoval, int entryToAdd, int bitCount, int numHashes) {
        CountingBloomFilter countingBloomFilter = new CountingBloomFilter(1000, 500, 500, 10000, 7);
        encode();
        remove();
        reAdd();
        int count = lookOriginalElements();
        writeToFile(count);

    }

    static void encode() {
        int[] hashVal;
        int id;
        for (int i = 0; i < numEntriesinitial; i++) {
            id = getRandomIntegerInitial();
            hashVal = getHash(id);
            for (int j = 0; j < hashes; j++) {
                bitArray[hashVal[j] % bitArrayLen] = bitArray[hashVal[j] % bitArrayLen] + 1;
            }
        }
    }


    static void writeToFile(int count)
    {
        BufferedWriter outputFile = null;
        try {
            File file = new File("CountingBloomFilter.txt");
            outputFile = new BufferedWriter(new FileWriter(file));
            outputFile.write("Total count of elements of A found after removal and readdition of elements in the filter " + String.valueOf(count));
            outputFile.newLine();
        } catch ( IOException e ) {
        } finally {
            if ( outputFile != null ) {
                try {
                    outputFile.close();
                } catch (IOException e) {
                }
            }
        }
    }


    static void remove() {
        int[] hashVal;
        int count = 0;
        for (int valToBeRemoved : uniqueValsInitial) {
            hashVal = getHash(valToBeRemoved);
            for (int j = 0; j < hashes; j++) {
                bitArray[hashVal[j] % bitArrayLen] = bitArray[hashVal[j] % bitArrayLen] - 1;
            }
            count++;
            if (count == numEntriesRemoved) {
                break;
            }
        }
    }

    static void reAdd() {
        int[] hashVal;
        int id;
        for (int i = 0; i < numEntriesAdded; i++) {
            id = getRandomIntegerToBeAdded();
            hashVal = getHash(id);
            for (int j = 0; j < hashes; j++) {
                bitArray[hashVal[j] % bitArrayLen] = bitArray[hashVal[j] % bitArrayLen] + 1;
            }
        }
    }

    static int lookOriginalElements() {

        int[] hashVal;

        int count = 0;
        boolean isPresent;
        for (int valForLookup : uniqueValsInitial) {
            hashVal = getHash(valForLookup);
            isPresent = true;
            for (int j = 0; j < hashes; j++) {
                if (bitArray[hashVal[j] % bitArrayLen] <= 0) {
                    isPresent = false;
                    break;
                }
            }
            if (isPresent) {
                count++;
            }
        }

        System.out.println("Total count of elements of A found after removal and readdition of elements in the filter " + count);
        return count;
    }

    static int[] getHash(int id) {
        int[] hashValues = new int[hashes];
        for (int i = 0; i < hashes; i++) {
            hashValues[i] = id ^ randValue[i];
        }
        return hashValues;
    }

    static int getRandomIntegerInitial() {
        int val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;// (max - min) + min
        while (globalSet.contains(val)) {
            val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;
        }
        globalSet.add(val);
        uniqueValsInitial.add(val);
        return val;
    }


    static int getRandomIntegerToBeAdded() {
        int val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;// (max - min) + min
        while (globalSet.contains(val)) {
            val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;
        }
        globalSet.add(val);
        uniqueValsToBeAdded.add(val);
        return val;
    }


}
