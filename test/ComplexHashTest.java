import complex_hash.ComplexHashTable;
import complex_hash.ComplexNumber;

import java.util.List;
import java.util.Random;

public class ComplexHashTest {
    public static void main(String[] args){
        ComplexHashTable table = new ComplexHashTable();
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            table.push(new ComplexNumber(random.nextInt(20), random.nextInt(20)));
        }

        table.print();
    }
}
