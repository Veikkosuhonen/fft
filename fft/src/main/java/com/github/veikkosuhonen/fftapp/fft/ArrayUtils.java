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
        return slice(arr, 0, end);
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param start The first index of the subsection
     * @param end The last index of the subsection
     * @return The sliced array
     */
    public static float[] slice(float[] arr, int start, int end) {
        float[] newArr = new float[end];
        System.arraycopy(arr, start, newArr, 0, end - start);
        return newArr;
    }

    public static float[] join(float[] arr1, float[] arr2) {
        float[] newArr = new float[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, newArr, 0, arr1.length);
        System.arraycopy(arr2, 0, newArr, arr1.length, arr2.length);
        return newArr;
    }
}
