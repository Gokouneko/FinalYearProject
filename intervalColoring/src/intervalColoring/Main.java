package intervalColoring;

import org.jfree.ui.RefineryUtilities;

public class Main {

    public static void main(String[] args) {

        Results chart = new Results(
                "IntervalColoring" ,
                "Skyline");
        chart.run();
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );


    }

}
