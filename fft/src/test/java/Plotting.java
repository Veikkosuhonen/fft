import com.github.veikkosuhonen.fftapp.fft.DFT;
import com.github.veikkosuhonen.fftapp.fft.FFT;
import com.github.veikkosuhonen.fftapp.fft.NaiveDFT;
import com.github.veikkosuhonen.fftapp.fft.ReferenceFFT;
import utils.Signal;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 * Used for visualizing DFT algorithms on generated signal
 */
public class Plotting {


    public static void main(String[] args) {

        double[][] signal = Signal.generateSineComposite(1024, new double[]{1.0, 10.0, 100.0});
        DFT dft = new FFT();
        double[][] fx = dft.process(signal);

        JPanel panel = new JPanel();
        panel.add(makePlot(signal[0], "Signal"));
        panel.add(makePlot(fx[0], "Fourier transform"));
        ScrollPane spane = new ScrollPane();
        spane.add(panel);
        JFrame frame = new JFrame();
        frame.add(spane);
        frame.setSize(1280, 720);
        frame.setVisible(true);
    }

    public static ChartPanel makePlot(double[] signal, String name) {
        XYSeries series = new XYSeries(name);
        for (int i = 0; i < signal.length; i++) {
            series.add(i, signal[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(name, "", "", dataset);
        return new ChartPanel(chart);
    }
}
