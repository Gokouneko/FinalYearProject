package intervalColoring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Results {
    Algorithm algorithm = new Algorithm();
    Interface Interface = new Interface();
    public Results(){
    }

    public void run(){
        algorithm.dataGenerating();
        calSkylineCost();
    }

    public void calSkylineCost(){

        ArrayList<ColoredInterval> coloredIntervalSet = readInterval();
        int range = algorithm.range;
        int skylineCost = 0;
        for(int i=0;i<range;i++){
            ArrayList<ColoredInterval>termList = new ArrayList<>();
            for (ColoredInterval termInterval: coloredIntervalSet) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                if((start <= i) && (end >= (i + 1))){
                    termList.add(termInterval);
                }
            }
            int cost = 0;
            if(termList.size()>1){
                ArrayList<Integer>costList = new ArrayList<>();
                for(ColoredInterval termInterval: termList){
                    costList.add(termInterval.getColor().getCost());
                }
                cost = Collections.max(costList);
            }else if(termList.size()==1){
                cost = termList.get(0).getColor().getCost();
            }
            skylineCost += cost;
        }
        System.out.println("Algorithm");
        for (ColoredInterval termInterval: coloredIntervalSet) {
            System.out.println("Interval: ["+ termInterval.getInterval().getCoordinate()[0]+ " " +termInterval.getInterval().getCoordinate()[1]+"] color: "+termInterval.getColor().getColor());

        }
        System.out.println("skyline cost = "+ skylineCost);


        Interface.chooseLoop();
        int change = Interface.change;
        if(change==1){
            algorithm.algorithmChoose();
            calSkylineCost();
        }
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
