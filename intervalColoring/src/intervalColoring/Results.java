package intervalColoring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Results {

    public Results(){
    }

    public void calSkylineCost(){
        ArrayList<ColoredInterval> coloredIntervalSet = readInterval();
        
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
