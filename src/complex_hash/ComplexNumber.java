package complex_hash;


public class ComplexNumber implements Comparable<ComplexNumber> {
    private final int real;
    private final int imaginary;

    public ComplexNumber(int real, int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public int getReal() {
        return real;
    }

    public int getImaginary() {
        return imaginary;
    }

    public int cantorNumber(){
        return Math.abs(((real+imaginary)*(real+imaginary+1))/2+real);
    }

    @Override
    public int compareTo(ComplexNumber o) {
        return this.cantorNumber() - o.cantorNumber();
    }

    @Override
    public String toString() {
        return real +
                (imaginary >= 0 ? "+" : "") + imaginary +
                'i';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return real == that.real && imaginary == that.imaginary;
    }

}
