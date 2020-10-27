import java.util.Scanner;

public class Main {

    public static void main(String [] args)
    {
        Scanner in = new Scanner(System.in);
        int option;
        while(true) {
            System.out.println("Enter 1: Bloom Filter 2: Counting Bloom Filter 3: Coded Bloom Filter 4: exit");
            option = Integer.parseInt(in.nextLine());
            switch (option) {
                case 1: BloomFilter.callBloomFilter(1000, 10000, 7);
                    break;
                case 2: CountingBloomFilter.callCountingBloomFilter(1000, 500, 500, 10000, 7);
                    break;
                case 3: CodedBloomFilter.callCodedBloomFilter(7, 1000, 3, 30000, 7);
                    break;
                case 4: System.exit(0);
                    break;
                default: System.out.println("Please Enter a Valid Option");
            }
        }
    }
}
