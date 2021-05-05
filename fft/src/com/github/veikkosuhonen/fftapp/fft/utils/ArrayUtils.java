package com.github.veikkosuhonen.fftapp.fft.utils;

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
     * @param end The index where the slice ends
     * @return The sliced array
     */
    public static float[] slice(float[] arr, int end) {
        return slice(arr, 0, end);
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param start The first index of the subsection
     * @param end The index where the slice ends
     * @return The sliced array
     */
    public static float[] slice(float[] arr, int start, int end) {
        if (end <= start) throw new IllegalArgumentException("End index of the slice must be greater than the start index");
        float[] newArr = new float[end - start];
        System.arraycopy(arr, start, newArr, 0, end - start);
        return newArr;
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param end The last index of the subsection
     * @return The sliced array
     */
    public static double[] slice(double[] arr, int end) {
        return slice(arr, 0, end);
    }

    /**
     * Creates a new array from a subsection of the original
     * @param arr The original array
     * @param start The first index of the subsection
     * @param end The last index of the subsection
     * @return The sliced array
     */
    public static double[] slice(double[] arr, int start, int end) {
        double[] newArr = new double[end];
        System.arraycopy(arr, start, newArr, 0, end - start);
        return newArr;
    }

    /**
     * Creates a new array with the contents of two arrays
     * @param arr1 first array
     * @param arr2 second array
     * @return new array with the second array appended to the first array
     */
    public static float[] join(float[] arr1, float[] arr2) {
        float[] newArr = new float[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, newArr, 0, arr1.length);
        System.arraycopy(arr2, 0, newArr, arr1.length, arr2.length);
        return newArr;
    }

    /**
     * Returns a new array with the absolute value of each element of the given array
     * @param arr The original array
     * @return The new array with absolute values
     */
    public static float[] abs(float[] arr) {
        float[] newArr = new float[arr.length];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i] < 0 ? -arr[i] : arr[i];
        }
        return newArr;
    }

    /**
     * Select the k:th element of the array in specified range using <a href=https://en.wikipedia.org/wiki/Quickselect>quickselect</a>
     * <p>For example, to calculate the median use {@code select(arr, 0, arr.length - 1, arr.length / 2)}</p>
     * @param arr0
     * @param left the starting index of the range
     * @param right the last index of the range
     * @param k specifies which element is chosen of the ordered elements
     * @return the median
     */
    public static float select(float[] arr0, int left, int right, int k) {
        float[] arr = new float[arr0.length];
        System.arraycopy(arr0, 0, arr, 0, arr.length);
        return selectInPlace(arr, left, right, k);
    }

    /**
     * Select the k:th element of the array in specified range using <a href=https://en.wikipedia.org/wiki/Quickselect>quickselect</a>
     * {@code selectInPlace} operates in place on the given array. If you don't want it to be modified, use {@code select} instead.
     * <p>For example, to calculate the median use {@code selectInPlace(arr, 0, arr.length - 1, arr.length / 2)}</p>
     * @param arr
     * @param left the starting index of the range
     * @param right the last index of the range
     * @param k specifies which element is chosen of the ordered elements
     * @return the median
     */
    public static float selectInPlace(float[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }
        int pivotIndex = left + (right - left) / 2;
        pivotIndex = partition(arr, left, right, pivotIndex);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return selectInPlace(arr, left, pivotIndex - 1, k);
        } else {
            return selectInPlace(arr, pivotIndex + 1, right, k);
        }
    }

    private static int partition(float[] arr, int left, int right, int pivotIndex) {
        float pivotValue = arr[pivotIndex];

        float temp = arr[right];
        arr[right] = pivotValue;
        arr[pivotIndex] = temp;

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (arr[i] < pivotValue) {
                temp = arr[i];
                arr[i] = arr[storeIndex];
                arr[storeIndex] = temp;
                storeIndex++;
            }
        }

        temp = arr[right];
        arr[right] = arr[storeIndex];
        arr[storeIndex] = temp;
        return storeIndex;
    }

    /**
     * Transforms the values in the array by scaling to the range (min, max)
     * @param arr the array to be modified
     * @param min the minimum of the range
     * @param max the maximum of the range
     * @return the modified array
     */
    public static float[] scale(float[] arr, float min, float max) {
        float oldMin = Float.MAX_VALUE;
        float oldMax = Float.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < oldMin) {
                oldMin = arr[i];
            }
            if (arr[i] > oldMax) {
                oldMax = arr[i];
            }
        }
        float scale = (max - min) / (oldMax - oldMin);
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= oldMin;
            arr[i] *= scale;
            arr[i] += min;
        }
        return arr;
    }

    /**
     * Transforms the values in the array by scaling from the range(oldMin, oldMax) to the range (min, max)
     * @param arr the array to be modified
     * @param oldMin the minimum of the original range
     * @param oldMax the maximum of the original range
     * @param min the minimum of the new range
     * @param max the maximum of the new range
     * @return the modified array
     */
    public static float[] scale(float[] arr, float oldMin, float oldMax, float min, float max) {
        float scale = (max - min) / (oldMax - oldMin);
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= oldMin;
            arr[i] *= scale;
            arr[i] += min;
        }
        return arr;
    }

    /**
     * Transforms the values in the array by clamping to the range (min, max)
     * @param arr the array to be modified
     * @param min the minimum of the range
     * @param max the maximum of the range
     * @return the modified array
     */
    public static float[] clamp(float[] arr, float min, float max) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                arr[i] = min;
            } else if (arr[i] > max) {
                arr[i] = max;
            }
        }
        return arr;
    }

    public static float max(float[] arr) {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max = arr[i] > max ? arr[i] : max;
        }
        return max;
    }
}
