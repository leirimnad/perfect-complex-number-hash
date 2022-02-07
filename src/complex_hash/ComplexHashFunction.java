package complex_hash;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class ComplexHashFunction {
    private final int a, b, p, m;

    public ComplexHashFunction(int a, int b, int p, int m) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.m = m;
    }

    public int hash(ComplexNumber complexNumber){
        return (int) (((Math.pow(a, 1)*Math.abs(complexNumber.getReal()) +
                        Math.pow(a, 2)*Math.abs(complexNumber.getImaginary()) +
                        Math.pow(a, 3)*positiveSgn(complexNumber.getReal())+
                        Math.pow(a, 4)*positiveSgn(complexNumber.getImaginary())+
                        b
                      ) % p) % m);
    }

    private int positiveSgn(int x){
        return (x >= 0 ? 1 : 0);
    }

    public static ComplexHashFunction getRandomHashFunction(int m, int p){
        Random random = new Random();

        int a = random.nextInt(p - 1) + 1;
        int b = random.nextInt(p);

        return new ComplexHashFunction(a, b, p, m);
    }

    @Override
    public String toString() {
        if (a == 0 && b == 0) return "<0>";
        return "<((" +
                a + "*|r| + " +
                (int) Math.pow(a,2) + "*|i| + " +
                (int) Math.pow(a,3) + "*f(r) + " +
                (int) Math.pow(a,4) + "*f(i) + " +
                b +
                ") % " + p +
                ") % " + m +
                '>';
    }



}
