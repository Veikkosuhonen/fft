package com.github.veikkosuhonen.fftapp.fft;

import java.util.Objects;

public class Complex {
    public double real, img;

    public Complex(double real, double img) {
        this.real = real;
        this.img = img;
    }

    public void add(Complex c) {
        this.real += c.real;
        this.img += c.img;
    }

    public void mult(Complex c) {
        double newReal = this.real * c.real - this.img * c.img;
        this.img = this.real * c.img + this.img * c.real;
        this.real = newReal;
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
