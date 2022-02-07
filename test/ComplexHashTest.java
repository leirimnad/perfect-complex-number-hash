import complex_hash.ComplexHashTable;
import complex_hash.ComplexNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComplexHashTest {
    public static void main(String[] args){
        Random random = new Random();
        List<ComplexNumber> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int a = random.nextInt(200);
            int b = random.nextInt(200);
            if(random.nextBoolean()) a *= -1;
            if(random.nextBoolean()) b *= -1;
            list.add(new ComplexNumber(a, b));
        }
        ComplexHashTable table = new ComplexHashTable(list.toArray(new ComplexNumber[0]));
        table.print();
    }
}
