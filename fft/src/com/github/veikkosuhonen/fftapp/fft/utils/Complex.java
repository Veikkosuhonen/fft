package com.github.veikkosuhonen.fftapp.fft.utils;

import java.util.Objects;

/**
 * Represents a complex number with a real and an imaginary part in double precision
 */
public class Complex {
    public double real, img;

    /**
     * Constructs a new complex number from a real and an imaginary part
     * @param real The real part
     * @param img The imaginary part
     */
    public Complex(double real, double img) {
        this.real = real;
        this.img = img;
    }

    /**
     * @param c The other complex number in binary addition
     * @return The result of the addition as a new complex number
     */
    public Complex plus(Complex c) {
        return new Complex(real + c.real, img + c.img);
    }

    /**
     * @param c The other complex number in binary negation
     * @return The result of the negation as a new complex number
     */
    public Complex minus(Complex c) {
        return new Complex(real - c.real, img - c.img);
    }

    /**
     * @param c The other complex number in multiplication
     * @return The result of the multiplication as a new complex number
     */
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
