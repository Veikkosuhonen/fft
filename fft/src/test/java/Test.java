import com.github.veikkosuhonen.fftapp.fft.dft.DFT;
import com.github.veikkosuhonen.fftapp.fft.dft.OptimizedInPlaceFFT;
import utils.Signal;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int n = 16;
        DFT dft = new OptimizedInPlaceFFT();
        double[][] result = dft.process(new double[][]{Signal.generateSineComposite(n, new double[]{3.0, 10.0}), new double[n]});
        System.out.println(Arrays.toString(result[0]));
    }
}
