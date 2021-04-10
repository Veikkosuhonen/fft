package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class ChartBuilder {

    private final XYSeriesCollection dataset;

    public ChartBuilder() {
        dataset = new XYSeriesCollection();
    }

    public ChartBuilder addSeries(double[] y, int[] x, String name) {
        if (y.length != x.length) {
            throw new IllegalArgumentException("X and y series must be equal length");
        }
        XYSeries series = new XYSeries(name);
        for (int i = 0; i < y.length; i++) {
            series.add(x[i], y[i]);
        }
        dataset.addSeries(series);
        return this;
    }

    public ChartPanel build(String name, String xAxis, String yAxis) {
        JFreeChart chart = ChartFactory.createXYLineChart(name, xAxis, yAxis, dataset);
        chart.getXYPlot().setBackgroundPaint(Color.BLACK);
        chart.setBackgroundPaint(Color.BLACK);
        chart.getLegend().setBackgroundPaint(Color.BLACK);
        return new ChartPanel(chart);
    }
}
