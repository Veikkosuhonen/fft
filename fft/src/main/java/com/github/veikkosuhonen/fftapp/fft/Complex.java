package com.github.veikkosuhonen.fftapp.fft;

import java.util.Objects;

/**
 * Represents a complex number with a real and an imaginary part in double precision
 */
public class Complex {
    public double real, img;

    public Complex(double real, double img) {
        this.real = real;
        this.img = img;
    }

    public Complex plus(Complex c) {
        return new Complex(real + c.real, img + c.img);
    }

    public Complex minus(Complex c) {
        return new Complex(real - c.real, img - c.img);
    }

    public Complex times(Complex c) {
        double newReal = this.real * c.real - this.img * c.img;
        double newImg = this.real * c.img + this.img * c.real;
        return new Complex(newReal, newImg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return complex.real == real && complex.img == img;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, img);
    }

    @Override
    public String toString() {
        return "[" + real + ", " + img + "i]";
    }
}
