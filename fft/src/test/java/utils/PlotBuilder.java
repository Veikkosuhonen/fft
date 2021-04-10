package utils;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

public class PlotBuilder {

    private final JPanel panel;

    public PlotBuilder() {
        this.panel = new JPanel();
        this.panel.setBackground(Color.DARK_GRAY);
    }

    public PlotBuilder addChart(ChartPanel chartPanel) {
        this.panel.add(chartPanel);
        return this;
    }

    public void show() {
        show(720, 720);
    }

    public void show(int width, int height) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(panel);
        JFrame frame = new JFrame();
        frame.add(scrollPane);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
}
