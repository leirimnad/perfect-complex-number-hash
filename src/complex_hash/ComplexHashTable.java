package complex_hash;

import java.util.*;


public class ComplexHashTable {
    private ComplexNumber[][] table;
    private ComplexHashFunction primaryHashFunction;
    private ComplexHashFunction[] secondaryHashFunctions;
    private int size = 0;
    private final float increaseOccupancy = 0.75f;

    public ComplexHashTable() {
        build(8);
    }

    public ComplexHashTable(ComplexNumber... complexNumbers) {
        build(Math.max(8, complexNumbers.length * 2), complexNumbers);
    }

    public void push(ComplexNumber number){
        // skip if one is cached
        if (this.contains(number)) return;

        // increase the size if there are too many elements
        if (size+1 > table.length * increaseOccupancy) {
            this.increaseSize();
        }

        int primaryHash = primaryHashFunction.hash(number);

        // regroup the secondary table
        Stack<ComplexNumber> rowNumbers = new Stack<>();
        for (ComplexNumber c : table[primaryHash]) {
            if (c != null) {
                rowNumbers.push(c);
            }
        }
        rowNumbers.push(number);

        buildSecondaryTable(primaryHash, rowNumbers.toArray(new ComplexNumber[0]));

        size++;

    }

    private void build(int size, ComplexNumber... complexNumbers)  {

        table = new ComplexNumber[size][];
        secondaryHashFunctions = new ComplexHashFunction[size];
        primaryHashFunction = ComplexHashFunction.getRandomHashFunction(size, complexNumbers);

        // getting chains for the new table
        List<List<ComplexNumber>> chains = new ArrayList<>(Collections.nCopies(size, null));

        for (ComplexNumber number : complexNumbers) {
            int hash = primaryHashFunction.hash(number);
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
            ComplexHashFunction newSecondaryFunction;
            table[primaryHash] = new ComplexNumber[(int) Math.pow(numbers.length, 2)];

            if (numbers.length <= 1)
                newSecondaryFunction = new ComplexHashFunction(0, 1, 1);
            else
                newSecondaryFunction = ComplexHashFunction.getRandomHashFunction(table[primaryHash].length, numbers);

            // setting and checking for collisions
            boolean collision = false;

            for (ComplexNumber num : numbers) {
                int secondaryHash = newSecondaryFunction.hash(num);

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
        int primaryHash = primaryHashFunction.hash(number);
        if (secondaryHashFunctions[primaryHash] == null) return false;
        int secondaryHash = secondaryHashFunctions[primaryHash].hash(number);

        if (       table.length <= primaryHash
                || table[primaryHash].length <= secondaryHash
                || table[primaryHash][secondaryHash] == null
        ) return false;

        return table[primaryHash][secondaryHash].equals(number);
    }

    public void delete(ComplexNumber number){
        int primaryHash = primaryHashFunction.hash(number);
        if (secondaryHashFunctions[primaryHash] == null) return;
        int secondaryHash = secondaryHashFunctions[primaryHash].hash(number);

        if (       table.length <= primaryHash
                || table[primaryHash].length <= secondaryHash
                || table[primaryHash][secondaryHash] == null
        ) return;

        table[primaryHash][secondaryHash] = null;
        size--;

        if (size < 0.2f * table.length && table.length > 8){
            List<ComplexNumber> list = this.getList();
            build(Math.max(8, list.size() * 2), list.toArray(new ComplexNumber[0]));
        }
    }

    private void increaseSize(){
        List<ComplexNumber> numbers = this.getList();

        try {
            build((int) (table.length * 1.5), numbers.toArray(new ComplexNumber[0]));
        } catch (Exception e) { e.printStackTrace(); }
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

}
