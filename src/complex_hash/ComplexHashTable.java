package complex_hash;


import java.util.Random;
import java.util.stream.IntStream;

public class ComplexHashTable {

    public ComplexHashTable() {
        // ...
    }

    public ComplexHashTable(ComplexNumber... complexNumbers) {
        // ...
    }

    public void push(ComplexNumber number){
        // ...
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
