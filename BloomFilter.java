import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BloomFilter {

    static int bitArray[];
    static int numEntries;
    static int hashes;
    static int[] randValue;
    static int bitArrayLen;
    static Set<Integer>
            uniqueVals1;
    static Set<Integer>
            uniqueVals2;

    static Set<Integer>
    globalSet;

    public BloomFilter(int entries, int numBits, int numHashes) {
        numEntries = entries;
        bitArray = new int[numBits];
        bitArrayLen = numBits;
        hashes = numHashes;
        uniqueVals1 = new HashSet<>();
        uniqueVals2 = new HashSet<>();
        globalSet = new HashSet<>();
        randValue = new int[numHashes];
        Random rd = new Random();

        for (int i = 0; i < numHashes; i++) {
            randValue[i] = 0 + rd.nextInt(Integer.MAX_VALUE);// min + rd.nextInt(maxValue)
        }
    }

    public static void callBloomFilter(int totEntries, int numOfBits, int totHashes) {
        BloomFilter bloomFilter = new BloomFilter(totEntries, numOfBits, totHashes);
        encode();
        int countA = checkSetAElementsPresent();
        int countB = checkSetBElementsPresent();
        writeToFile(countA, countB);
    }

    static void encode() {
        int[] hashVal;
        int id;
        for (int i = 0; i < numEntries; i++) {
            id = getRandomIntegerForSet1();
            hashVal = getHash(id);
            for (int j = 0; j < hashes; j++) {
                bitArray[hashVal[j] % bitArrayLen] = 1;
            }
        }
    }


    static void writeToFile(int count1, int count2)
    {
        BufferedWriter outputFile = null;
        try {
            File file = new File("BloomFilter.txt");
            outputFile = new BufferedWriter(new FileWriter(file));
            outputFile.write("The number of elements present in Bloom Filter from Set A is " + String.valueOf(count1));
            outputFile.newLine();
            outputFile.write("The number of elements present in Bloom Filter from Set B is " + String.valueOf(count2));
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


    static int checkSetAElementsPresent() {
        int count = 0;
        int[] hashVal;
        boolean isPresent;
        for (int val : uniqueVals1) {
            isPresent = true;
            hashVal = getHash(val);
            for (int j = 0; j < hashes; j++) {
                if (bitArray[hashVal[j] % bitArrayLen] != 1) {
                    isPresent = false;
                    break;
                }
            }
            if (isPresent) {
                count++;
            }
        }
        System.out.println("The number of elements present in Bloom Filter from Set A is " + count);
        return count;
    }


    static int checkSetBElementsPresent() {
        for (int i = 0; i < numEntries; i++) {
            getRandomIntegerForSet2();
        }
        int count = 0;
        int[] hashVal = new int[hashes];
        boolean isPresent;
        for (int val : uniqueVals2) {
            isPresent = true;
            hashVal = getHash(val);
            for (int j = 0; j < hashes; j++) {
                if (bitArray[hashVal[j] % bitArrayLen] != 1) {
                    isPresent = false;
                    break;
                }
            }
            if (isPresent) {
                count++;
            }
        }
        System.out.println("The number of elements present in Bloom Filter from Set B is " + count);
        return count;
    }

    static int[] getHash(int id) {
        int[] hashValues = new int[hashes];
        for (int i = 0; i < hashes; i++) {
            hashValues[i] = id ^ randValue[i];
        }
        return hashValues;
    }

    static int getRandomIntegerForSet1() {
        int val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;// (max - min) + min
        while (globalSet.contains(val)) {
            val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;
        }
        uniqueVals1.add(val);
        globalSet.add(val);
        return val;
    }

    static int getRandomIntegerForSet2() {
        int val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;// (max - min) + min
        while (globalSet.contains(val)) {
            val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;
        }
        uniqueVals2.add(val);
        globalSet.add(val);
        return val;
    }
}
