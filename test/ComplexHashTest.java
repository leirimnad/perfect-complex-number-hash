import complex_hash.ComplexHashTable;
import complex_hash.ComplexNumber;

import java.util.Random;

public class ComplexHashTest {
    public static void main(String[] args){
        ComplexHashTable table = new ComplexHashTable();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int a = random.nextInt(200);
            int b = random.nextInt(200);
            if(random.nextBoolean()) a *= -1;
            if(random.nextBoolean()) b *= -1;
            table.push(new ComplexNumber(a, b));
        }

        table.print();
    }
}
