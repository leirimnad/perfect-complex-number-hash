package complex_hash;

public class ComplexNumber implements Comparable<ComplexNumber> {
    private int real, imaginary;

    public ComplexNumber(int real, int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public int getReal() {
        return real;
    }

    public void setReal(int real) {
        this.real = real;
    }

    public int getImaginary() {
        return imaginary;
    }

    public void setImaginary(int imaginary) {
        this.imaginary = imaginary;
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
                "+" + imaginary +
                'i';
    }


}
