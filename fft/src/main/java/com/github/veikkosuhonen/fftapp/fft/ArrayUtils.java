package com.github.veikkosuhonen.fftapp.fft;

public class ArrayUtils {

    /**
     * Converts a double array to float array
     * @param arr The double array to be converted
     * @return a float array converted from the double array
     */
    public static float[] toFloatArray(double[] arr) {
        float[] farr = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            farr[i] = (float) arr[i];
        }
        return farr;
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param end The last index of the subsection
     * @return The sliced array
     */
    public static float[] slice(float[] arr, int end) {
        float[] farr = new float[end];
        System.arraycopy(arr, 0, farr, 0, end);
        return farr;
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param start The first index of the subsection
     * @param end The last index of the subsection
     * @return The sliced array
     */
    public static float[] slice(float[] arr, int start, int end) {
        float[] farr = new float[end];
        System.arraycopy(arr, start, farr, 0, end - start);
        return farr;
    }
}
