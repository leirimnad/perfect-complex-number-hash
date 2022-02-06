package complex_hash;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class ComplexHashFunction {
    private final int a, p, m;

    public ComplexHashFunction(int a, int p, int m) {
        this.a = a;
        this.p = p;
        this.m = m;
    }

    public int hash(ComplexNumber complexNumber){
        return (int) (((Math.pow(a, 1)*Math.abs(complexNumber.getReal()) +
                        Math.pow(a, 2)*Math.abs(complexNumber.getImaginary()) +
                        Math.pow(a, 3)*positiveSgn(complexNumber.getReal())+
                        Math.pow(a, 4)*positiveSgn(complexNumber.getImaginary())
                      ) % p) % m);
    }

    private int positiveSgn(int x){
        return (x >= 0 ? 1 : 0);
    }

    public static ComplexHashFunction getRandomHashFunction(int m, ComplexNumber... numbers){
        Random random = new Random();

        Optional<ComplexNumber> maxO = Arrays.stream(numbers).max(
                Comparator.comparingInt(a -> Math.max(a.getReal(), a.getImaginary()))
        );
        ComplexNumber max = maxO.orElse(new ComplexNumber(1,1));

        int p = Math.max(Math.max(max.getReal(), max.getImaginary()), m);

        do {
            for (int i = p+1;; i++) {
                if (isPrime(i)) {
                    p = i;
                    break;
                }
            }
        } while (random.nextBoolean());


        int a = random.nextInt(p - 1) + 1;

        return new ComplexHashFunction(a, p, m);
    }

    @Override
    public String toString() {
        return "<((" +
                a + "*|r| + " +
                (int) Math.pow(a,2) + "*|i| + " +
                (int) Math.pow(a,3) + "*f(r) + " +
                (int) Math.pow(a,4) + "*f(i)" +
                ") % " + p +
                ") % " + m +
                '>';
    }

    private static boolean isPrime(int number) {
        return number > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

}
