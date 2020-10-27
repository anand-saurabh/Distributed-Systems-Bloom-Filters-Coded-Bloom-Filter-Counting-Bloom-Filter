import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodedBloomFilter {
    static int[] randValue;
    static int numSets;
    static int eleInEachSet;
    static int numFilters;
    static int bitsPerFilter;
    static int numHashes;
    static int bloomFilters[][];
    static Set<Integer> globalSet;

    static Map<Integer, Set<Integer>> elePerSetMap;

    static Map<Integer, int[]> filterCodeMap;


    public CodedBloomFilter(int sets, int elePerSet, int totFilters, int bits, int hashes) {
        numSets = sets;
        eleInEachSet = elePerSet;
        numFilters = totFilters;
        bitsPerFilter = bits;
        numHashes = hashes;
        globalSet = new HashSet();
        Random rd = new Random();
        randValue = new int[numHashes];
        elePerSetMap = new HashMap<>();
        filterCodeMap = new HashMap<>();

        // set code
        filterCodeMap.put(1, new int[]{0, 0, 1});
        filterCodeMap.put(2, new int[]{0, 1, 0});
        filterCodeMap.put(3, new int[]{0, 1, 1});
        filterCodeMap.put(4, new int[]{1, 0, 0});
        filterCodeMap.put(5, new int[]{1, 0, 1});
        filterCodeMap.put(6, new int[]{1, 1, 0});
        filterCodeMap.put(7, new int[]{1, 1, 1});


        for (int i = 1; i <= sets; i++) {
            elePerSetMap.put(i, new HashSet<>());
        }

        bloomFilters = new int[totFilters][bits];
        for (int i = 0; i < numHashes; i++) {
            randValue[i] = 0 + rd.nextInt(Integer.MAX_VALUE);// min + rd.nextInt(maxValue)
        }
    }

    public static void callCodedBloomFilter(int totSet, int elementsPerSet, int totalFilterCount, int bitsInFilter, int hashesTot) {
        CodedBloomFilter codedBloomFilter = new CodedBloomFilter(totSet, elementsPerSet, totalFilterCount, bitsInFilter, hashesTot);
        encode();
        int count = performLookupForElements();
        writeToFile(count);
    }

    static int performLookupForElements()
    {
        Set<Integer>
                elements;
        int []hashes;
        StringBuffer codeFormed;
        int count = 0;
        boolean isPresent = true;
        StringBuffer filterCode;
        for (int i = 1; i <= numSets; i++) {

            filterCode = new StringBuffer();
            int [] temp = filterCodeMap.get(i);
            int tempLen = temp.length;
            for (int l = 0; l < tempLen; l++) {
                filterCode.append((char)(temp[l] + '0'));
            }

            elements = elePerSetMap.get(i);
            for (int ele : elements)
            {
                codeFormed = new StringBuffer();
                hashes = getHash(ele);

                // matching for each filter
                for (int j = 0; j < numFilters; j++) {
                    isPresent = true;
                    for (int k = 0; k < numHashes; k++)
                    {
                        if(bloomFilters[j][hashes[k] % bitsPerFilter] != 1) {
                            isPresent = false;
                            break;
                        }
                    }
                    if(isPresent)
                    {
                        codeFormed.append("1");
                    }
                    else
                    {
                        codeFormed.append("0");
                    }
                }
                if(codeFormed.toString().equals(filterCode.toString()))
                {
                    count++;
                }
            }
        }
        System.out.println("The total number of elements with correct lookup results " + count);
        return count;
    }
    static void encode() {
        int[] hashVal;
        int[] filCode;
        int filCodeLen;
        int bloomFilNum;
        for (int n = 1; n <= numSets; n++) {
            // get the code for the set number : n
            filCode = filterCodeMap.get(n);
            filCodeLen = filCode.length;
            getRandomInteger(n);
            for (int index = 0; index < filCodeLen; index++) {
                if (filCode[index] == 0) {
                    continue;
                }
                bloomFilNum = index;
                for (int id : elePerSetMap.get(n)) {
                    hashVal = getHash(id);
                    for (int j = 0; j < numHashes; j++) {
                        bloomFilters[bloomFilNum][hashVal[j] % bitsPerFilter] = 1;
                    }
                }
            }
        }
    }


    static void writeToFile(int count)
    {
        BufferedWriter outputFile = null;
        try {
            File file = new File("CodedBloomFilter.txt");
            outputFile = new BufferedWriter(new FileWriter(file));
            outputFile.write("The total number of elements with correct lookup results " + String.valueOf(count));
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


    static int[] getHash(int id) {
        int[] hashValues = new int[numHashes];
        for (int i = 0; i < numHashes; i++) {
            hashValues[i] = id ^ randValue[i];
        }
        return hashValues;
    }

    static void getRandomInteger(int setNum) {

        Set<Integer>
                temp = elePerSetMap.get(setNum);
        int val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;// (max - min) + min
        while (temp.size() != eleInEachSet) {
            while (globalSet.contains(val)) {
                val = ((int) (Math.random() * (Integer.MAX_VALUE - 1))) + 1;
            }
            temp.add(val);
            globalSet.add(val);
        }
    }
}
