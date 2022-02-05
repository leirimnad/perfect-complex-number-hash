package complex_hash;

import java.util.*;
import java.util.stream.IntStream;


public class ComplexHashTable {
    private final ComplexNumber[][] table;
    private final HashFunction primaryHashFunction;
    private HashFunction[] secondaryHashFunctions;
    private int size = 0;

    public ComplexHashTable() {
        table = new ComplexNumber[8][];
        primaryHashFunction = getRandomHashFunction(100, table.length);
    }

    public ComplexHashTable(ComplexNumber... complexNumbers) {
        table = new ComplexNumber[Math.max(8, complexNumbers.length * 2)][];
        ComplexNumber max = Arrays.stream(complexNumbers).max(Comparator.comparing(ComplexNumber::cantorNumber)).get();
        primaryHashFunction = getRandomHashFunction(max.cantorNumber(), table.length);
    }

    public void push(ComplexNumber number){
        // increase the size if there are too many elements
        if (size+1 > table.length * 0.75f) {
            // this.increaseSize();
        }

        int primaryHash = hashComplex(number, primaryHashFunction);

        // regroup the secondary table
        Stack<ComplexNumber> rowNumbers = new Stack<>();
        rowNumbers.push(number);
        for (ComplexNumber c : table[primaryHash]) {
            if (c != null) rowNumbers.push(c);
        }

        while (true) {
            // searching for a new secondary function
            HashFunction newSecondaryFunction;
            table[primaryHash] = new ComplexNumber[(int) Math.pow(rowNumbers.size(), 2)];

            if (rowNumbers.size() == 1)
                newSecondaryFunction = new HashFunction(0, 0, 0, 0);
            else
                newSecondaryFunction = getRandomHashFunction(
                        Collections.max(rowNumbers).cantorNumber(),
                        rowNumbers.size()
                );

            // setting and checking for collisions
            boolean collision = false;
            while (!rowNumbers.isEmpty()) {
                ComplexNumber num = rowNumbers.pop();
                int secondaryHash = newSecondaryFunction.hash(num.cantorNumber());

                if (table[primaryHash][secondaryHash] != null) {
                    collision = true;
                    break;
                }

                table[primaryHash][secondaryHash] = num;
            }

            if (!collision) {
                secondaryHashFunctions[primaryHash] = newSecondaryFunction;
                break;
            }
        }

        this.size++;
    }

    public boolean contains(ComplexNumber number){
        // ...
        return false;
    }

    private static int hashComplex(ComplexNumber number, HashFunction hashFunction){
        int sum = number.getReal() + number.getImaginary();
        int cantorNumber = (sum * (sum + 1)) / 2;
        return hashFunction.hash(cantorNumber);
    }

    private HashFunction getRandomHashFunction(int max, int m){
        int p;
        for (int i = Math.max(max, m)+1;; i++) {
            if (isPrime(i)) {
                p = i;
                break;
            }
        }

        Random random = new Random();
        int a = random.nextInt(p - 1) + 1;
        int b = random.nextInt(p);

        return new HashFunction(a, b, p, m);
    }

    private static class HashFunction {
        private final int a, b, p, m;

        public HashFunction(int a, int b, int p, int m) {
            this.a = a;
            this.b = b;
            this.p = p;
            this.m = m;
        }

        public int hash(int k){
            return ((a*k + b) % p) % m;
        }

    }

    private boolean isPrime(int number) {
        return number > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

}
