import com.github.veikkosuhonen.fftapp.fft.dct.DCT;
import com.github.veikkosuhonen.fftapp.fft.dct.FastDCT;
import com.github.veikkosuhonen.fftapp.fft.dct.RealOnlyDFT;
import com.github.veikkosuhonen.fftapp.fft.dft.ReferenceFFT;
import utils.ChartBuilder;
import utils.PlotBuilder;
import utils.Signal;

public class Test {
    public static void main(String[] args) {
        DCT dct = new FastDCT();
        DCT dct1 = new RealOnlyDFT(new ReferenceFFT());
        double[] signal = Signal.generateSineComposite(512, new double[] {1.0, 10.0, 100.0});
        double[] fx = dct.process(signal);
        double[] fx1 = dct1.process(signal);
        new PlotBuilder()
                .addChart(new ChartBuilder()
                        .addSeries(fx, "DCT")
                        .addSeries(fx1, "DCT1")
                        .build("Discrete Fourier Transform", "frequency", "magnitude"))
                .show();
    }
}
