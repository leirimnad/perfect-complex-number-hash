import complex_hash.ComplexHashTable;
import complex_hash.ComplexNumber;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTest {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Programming\\GitHub\\perfect-complex-number-hash\\test\\file.txt");

        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);

        List<ComplexNumber> numbers = new ArrayList<>();

        String line = reader.readLine();

        while (line != null) {

            ComplexNumber complexNumber = parseComplex(line);
            numbers.add(complexNumber);

            line = reader.readLine();
        }
        ComplexHashTable table = new ComplexHashTable(numbers.toArray(new ComplexNumber[0]));
        table.print();

        Random random = new Random();

        if (numbers.size() > 0){
            ComplexNumber randomFromList = (ComplexNumber) numbers.toArray()[random.nextInt(numbers.size())];
            System.out.println("\nRandomly picked element: "+randomFromList);
            System.out.println("table.contains(): "+table.contains(randomFromList));
        }

        ComplexNumber randomNumber = new ComplexNumber(random.nextInt(1000), random.nextInt(1000));
        System.out.println("\nRandomly generated element: "+randomNumber);
        System.out.println("table.contains(): "+table.contains(randomNumber));
    }

    public static ComplexNumber parseComplex(String passedString){
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(passedString);
        String real = "0";
        String imag = "0";
        if(m.find()){
            real = m.group(0);
            if(m.find()){
                imag = m.group(0);
            }
        }

        int realNum = Integer.parseInt(real);
        int imagNum = Integer.parseInt(imag);

        return new ComplexNumber(realNum, imagNum);
    }
}
