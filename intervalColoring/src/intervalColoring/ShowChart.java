package intervalColoring;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ShowChart extends ApplicationFrame {
    public ShowChart(String applicationTitle , String chartTitle){
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Coordinate" ,
                "Color" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                false , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );


    }

    private XYDataset createDataset( ){
        ArrayList<ColoredInterval> coloredIntervalSet = readInterval();
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(ColoredInterval termInterval: coloredIntervalSet){
            XYSeries interval = new XYSeries("interval");
            interval.add(termInterval.getInterval().getCoordinate()[0],termInterval.getColor().getColor());
            interval.add(termInterval.getInterval().getCoordinate()[1],termInterval.getColor().getColor());
            dataset.addSeries(interval);
        }
        return dataset;
    }

    public ArrayList<ColoredInterval> readInterval() {
        ArrayList<ColoredInterval> coloredIntervalSet = null;

        try {
            FileInputStream fileIn = new FileInputStream("souse/coloredInterval.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            coloredIntervalSet = (ArrayList<ColoredInterval>)in.readObject();
            in.close();
            fileIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return coloredIntervalSet;
    }
}
