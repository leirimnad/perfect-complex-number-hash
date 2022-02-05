package complex_hash;

import java.util.*;
import java.util.stream.IntStream;


public class ComplexHashTable {
    private ComplexNumber[][] table;
    private HashFunction primaryHashFunction;
    private HashFunction[] secondaryHashFunctions;
    private int size = 0;
    private int maxCantor = 0;

    public ComplexHashTable() {
        try {
            build(8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ComplexHashTable(ComplexNumber... complexNumbers) {
        try {
            build(Math.max(8, complexNumbers.length * 2), complexNumbers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(ComplexNumber number){
        // increase the size if there are too many elements
        if (size+1 > table.length * 0.75f) {
            this.increaseSize();
        }

        // rebuild a table if new value is max
        if (number.cantorNumber() > this.maxCantor){
            maxCantor = number.cantorNumber();
            List<ComplexNumber> numbers = getList();
            numbers.add(number);

            try {
                build(this.table.length, numbers.toArray(new ComplexNumber[0]));
            } catch (Exception e) { e.printStackTrace(); }
            return;
        }

        int primaryHash = primaryHashFunction.hash(number.cantorNumber());

        // regroup the secondary table
        Stack<ComplexNumber> rowNumbers = new Stack<>();

        boolean duplicate = false;
        for (ComplexNumber c : table[primaryHash]) {
            if (c != null) {
                rowNumbers.push(c);
                if (c.getReal() == number.getReal() && c.getImaginary() == number.getImaginary())
                    duplicate = true;
            }
        }
        if(!duplicate) {
            rowNumbers.push(number);
            this.size++;
        }

        buildSecondaryTable(primaryHash, rowNumbers.toArray(new ComplexNumber[0]));

    }

    private void build(int size, ComplexNumber... complexNumbers) throws Exception {
        if (complexNumbers.length > size) throw new Exception();

        table = new ComplexNumber[size][];
        secondaryHashFunctions = new HashFunction[size];
        int max;
        Optional<ComplexNumber> maxO = Arrays.stream(complexNumbers).max(Comparator.comparing(ComplexNumber::cantorNumber));
        max = maxO.map(ComplexNumber::cantorNumber).orElse(6);
        primaryHashFunction = getRandomHashFunction(max, size);

        // getting chains for the new table
        List<List<ComplexNumber>> chains = new ArrayList<>(Collections.nCopies(size, null));

        for (ComplexNumber number : complexNumbers) {
            int hash = primaryHashFunction.hash(number.cantorNumber());
            List<ComplexNumber> list = chains.get(hash);
            if (list == null) {
                list = new Stack<>();
                chains.set(hash, list);
            }
            list.add(number);
        }

        // building secondary tables
        for (int i = 0; i < size; i++) {
            List<ComplexNumber> list = chains.get(i);
            if (list == null || list.isEmpty())
                buildSecondaryTable(i);
            else
                buildSecondaryTable(i, list.toArray(new ComplexNumber[0]));
        }

        this.size = complexNumbers.length;
    }

    private void buildSecondaryTable(int primaryHash, ComplexNumber... numbers){

        while (true) {
            // searching for a new secondary function
            HashFunction newSecondaryFunction;
            table[primaryHash] = new ComplexNumber[(int) Math.pow(numbers.length, 2)];

            if (numbers.length <= 1)
                newSecondaryFunction = new HashFunction(0, 0, 1, 1);
            else
                newSecondaryFunction = getRandomHashFunction(
                        Arrays.stream(numbers).max(Comparator.comparing(ComplexNumber::cantorNumber))
                                .orElse(new ComplexNumber(0,0)).cantorNumber(),
                        table[primaryHash].length
                );

            // setting and checking for collisions
            boolean collision = false;

            for (ComplexNumber num : numbers) {
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

    }


    public boolean contains(ComplexNumber number){
        int primaryHash = primaryHashFunction.hash(number.cantorNumber());
        if (secondaryHashFunctions[primaryHash] == null) return false;
        int secondaryHash = secondaryHashFunctions[primaryHash].hash(number.cantorNumber());
        return table[primaryHash][secondaryHash].equals(number);
    }

    private void increaseSize(){
        List<ComplexNumber> numbers = this.getList();

        try {
            build((int) (table.length * 1.5), numbers.toArray(new ComplexNumber[0]));
        } catch (Exception e) { e.printStackTrace(); }
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

        @Override
        public String toString() {
            return "HashFunction <((" +
                    a +
                    "*k + " + b +
                    ") % " + p +
                    ") % " + m +
                    '>';
        }
    }

    private boolean isPrime(int number) {
        return number > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    private List<ComplexNumber> getList(){
        List<ComplexNumber> numbers = new ArrayList<>();
        for (ComplexNumber[] complexNumbers : table) {
            for (ComplexNumber c : complexNumbers) {
                if (c != null) numbers.add(c);
            }
        }
        return numbers;
    }

    public void print(){
        System.out.println("\nTable of " + this.size + " elements");
        System.out.println("Primary hash function: " + primaryHashFunction.toString());
        for (int i = 0; i < table.length; i++) {
            System.out.print("<"+ i + "> " + secondaryHashFunctions[i] + " |");
            for (ComplexNumber c : table[i]) {
                System.out.print(c + "|");
            }
            System.out.print('\n');
        }

    }

}
