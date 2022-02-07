package complex_hash;

import java.util.*;
import java.util.stream.IntStream;


public class ComplexHashTable {
    private ComplexNumber[][] table;
    private ComplexHashFunction primaryHashFunction;
    private ComplexHashFunction[] secondaryHashFunctions;
    private int size = 0;
    private final boolean preliminaryDuplicateCheck = false;
    private int p;

    public ComplexHashTable() {
        build();
    }

    public ComplexHashTable(ComplexNumber... complexNumbers) {
        build(complexNumbers);
    }

    private void build(ComplexNumber... complexNumbers)  {
        List<ComplexNumber> numbers = new ArrayList<>();

        // preliminary duplicate check
        if (!preliminaryDuplicateCheck) numbers = Arrays.asList(complexNumbers);
        else {
            for (ComplexNumber num : complexNumbers) {
                boolean duplicate = false;
                for (ComplexNumber num2 : numbers) {
                    if (num.equals(num2)){
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) numbers.add(num);
            }
        }

        int m = numbers.size();
        p = generateP(m);
        table = new ComplexNumber[m][];
        secondaryHashFunctions = new ComplexHashFunction[m];
        primaryHashFunction = ComplexHashFunction.getRandomHashFunction(m, p);

        // getting chains for the new table
        List<List<ComplexNumber>> chains = new ArrayList<>(Collections.nCopies(m, null));

        for (ComplexNumber number : numbers) {
            int hash = primaryHashFunction.hash(number);
            List<ComplexNumber> list = chains.get(hash);
            if (list == null) {
                list = new Stack<>();
                chains.set(hash, list);
            }
            list.add(number);
        }

        // building secondary tables
        for (int i = 0; i < m; i++) {
            List<ComplexNumber> list = chains.get(i);
            if (list == null || list.isEmpty())
                buildSecondaryTable(i);
            else
                buildSecondaryTable(i, list.toArray(new ComplexNumber[0]));
        }
    }

    private int generateP(int m){
        int p = m;

        for (int i = p+1;; i++) {
            if (isPrime(i)) {
                p = i;
                break;
            }
        }

        return p;
    }

    private void buildSecondaryTable(int primaryHash, ComplexNumber... numbers){

        while (true) {
            // searching for a new secondary function
            ComplexHashFunction newSecondaryFunction;

            List<ComplexNumber> duplicateFiltered = new ArrayList<>();

            // duplicate check
            if (preliminaryDuplicateCheck) duplicateFiltered = Arrays.asList(numbers);
            else {
                for (ComplexNumber num : numbers) {
                    boolean duplicate = false;
                    for (ComplexNumber num2 : duplicateFiltered) {
                        if (num.equals(num2)){
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate) duplicateFiltered.add(num);
                }
            }


            table[primaryHash] = new ComplexNumber[(int) Math.pow(duplicateFiltered.size(), 2)];

            if (numbers.length <= 1)
                newSecondaryFunction = new ComplexHashFunction(0,0, 1, 1);
            else
                newSecondaryFunction = ComplexHashFunction.getRandomHashFunction(table[primaryHash].length, p);

            // setting and checking for collisions
            boolean collision = false;

            for (ComplexNumber num : duplicateFiltered) {
                int secondaryHash = newSecondaryFunction.hash(num);

                if (table[primaryHash][secondaryHash] != null) {
                    collision = true;
                    break;
                }

                table[primaryHash][secondaryHash] = num;
                size++;
            }

            if (!collision) {
                secondaryHashFunctions[primaryHash] = newSecondaryFunction;
                break;
            }
        }

    }


    public boolean contains(ComplexNumber number){
        int primaryHash = primaryHashFunction.hash(number);
        if (secondaryHashFunctions[primaryHash] == null) return false;
        int secondaryHash = secondaryHashFunctions[primaryHash].hash(number);

        if (       table.length <= primaryHash
                || table[primaryHash].length <= secondaryHash
                || table[primaryHash][secondaryHash] == null
        ) return false;

        return table[primaryHash][secondaryHash].equals(number);
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
            StringBuilder text = new StringBuilder();
            text.append("<").append(i).append("> ").append("\t|");
            for (ComplexNumber c : table[i]) {
                text.append(c).append("|");
            }
            while (text.length() < 100) text.append(" ");
            text.append(secondaryHashFunctions[i]);
            System.out.println(text);
        }

    }

    private static boolean isPrime(int number) {
        return number > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

}
