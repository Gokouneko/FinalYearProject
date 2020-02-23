package intervalColoring;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Algorithm {
    DataGenerate dataGenerate = new DataGenerate();
    ArrayList<ArrayList<Color>> classifiedColor = new ArrayList<>();
    ArrayList<ArrayList<Integer>> classifiedColorId = new ArrayList<>();
    ArrayList<ColoredInterval> coloredIntervalList = new ArrayList<>();
    ArrayList<ArrayList<ColoredInterval>> classifiedcoloredIntervalList = new ArrayList<>();

    public Algorithm() {
    }

    public void onlineAlgorithm() {
        dataGenerate.dataTypeChoose();
        double lMax = dataGenerate.range;
        double lMin = 1;
        int L = 1 + (int) Math.ceil(Math.log(lMax / lMin) / Math.log(2));
        ArrayList<Interval> uncoloredList = readInterval();
        ArrayList<Color> colorSet = new ArrayList<>();

        /* Generate color set */
        for (int i = 0; i < uncoloredList.size(); i++) {
            ArrayList<ColoredInterval> sameColor = new ArrayList<>();
            Color color = new Color(i, i);
            colorSet.add(color);
            classifiedcoloredIntervalList.add(sameColor);
        }

        /* Color classification */
        int count = (int) Math.ceil((double) uncoloredList.size() / (double) L);
        int[] lengthId = new int[L];
        for (int i = 0; i < L; i++) {
            ArrayList<Color> termList = new ArrayList<>();
            int[] id = new int[count + 1];
            for (int j = 0; j <= count; j++) {
                id[j] = i + j * L;
            }
            int index = 0;
            for (Color color : colorSet) {
                int type = color.getColor();
                if (type == id[index]) {
                    termList.add(color);
                    index++;
                }
            }
            classifiedColor.add(termList);
            /* Find all the possible length of intervals */
            lengthId[i] = (int) (lMin * Math.pow(2, i));
        }

        /* Initial an ArrayList to store the index of each color */
        initIdList(L);

        /* Assign color to interval */
        for (Interval uncoloredInterval : uncoloredList) {
            int length = uncoloredInterval.getLength();
            for (int i = 0; i < lengthId.length; i++) {
                int index;
                /* Find the interval set which the current interval should in */
                if (length >= lengthId[i] && length < lengthId[i + 1]) {
                    Color color;
                    /* Find if there is some overlapping intervals */
                    if (isOverlapping(uncoloredInterval, i)) {

                        if (classifiedColorId.get(i).size() == 0) {
                            index = 0;
                        } else {

                            index = classifiedColorId.get(i).get(classifiedColorId.get(i).size()-1)+1;
                            //What if there are not enough color in colorSet i??
                        }
                        try{
                            color = classifiedColor.get(i).get(index);
                        }catch(Exception e){
                            expandColorSet(L, i);
                            color = classifiedColor.get(i).get(index);
                            if(color.getColor()>classifiedcoloredIntervalList.size()){
                                int term = color.getColor()-classifiedcoloredIntervalList.size();
                                for(int j=0;j<=term;j++){
                                    ArrayList<ColoredInterval> sameColor = new ArrayList<>();
                                    classifiedcoloredIntervalList.add(sameColor);
                                }
                            }
                        }

                        classifiedColorId.get(i).add(index);

                    } else {
                        ArrayList<ColoredInterval> notOverlappingList = getNotOverlappingList(uncoloredInterval, i);
                        ArrayList<Integer> colorId = new ArrayList<>();
                        /* Find all the possible color the interval can be assigned */
                        for (ColoredInterval termInterval : notOverlappingList) {
                            colorId.add(termInterval.getColor().getColor());
                        }
                        /* Give the interval a smallest cost color it can be assigned */
                        int id = Collections.min(colorId);
                        color = new Color(id, id);

                    }
                    ColoredInterval coloredInterval = new ColoredInterval(uncoloredInterval, color);
                    int colorId = color.getColor();
                    classifiedcoloredIntervalList.get(colorId).add(coloredInterval);
                    coloredIntervalList.add(coloredInterval);
                }
            }
        }

        saveInterval();

    }

    public boolean isOverlapping(Interval interval, int index) {

        boolean overlapping = true;
        int[] coordinate = interval.getCoordinate();
        for (Color termColor : classifiedColor.get(index)) {
            int colorId = termColor.getColor();
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                /* Judge if there is an interval that not overlapping with the current interval */
                if (start >= coordinate[1] || end <= coordinate[0]) {
                    overlapping = false;
                    break;
                }
            }
        }
        return overlapping;
    }

    public ArrayList<ColoredInterval> getNotOverlappingList(Interval interval, int index) {
        ArrayList<ColoredInterval> notOverlappingList = new ArrayList<>();
        int[] coordinate = interval.getCoordinate();
        for (Color termColor : classifiedColor.get(index)) {
            int colorId = termColor.getColor();
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                /* Find interval that not overlapping with the current interval */
                if (start >= coordinate[1] || end <= coordinate[0]) {
                    notOverlappingList.add(termInterval);
                }
            }
        }
        return notOverlappingList;
    }

    public ArrayList<Interval> readInterval() {
        ArrayList<Interval> intervalSet = null;

        try {
            FileInputStream fileIn = new FileInputStream("souse/Interval.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            intervalSet = (ArrayList<Interval>)in.readObject();
            in.close();
            fileIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return intervalSet;
    }

    public void initIdList(int L){
        for (int i = 0; i < L; i++) {
            ArrayList<Integer> termList = new ArrayList<>();
            classifiedColorId.add(termList);
        }
    }

    public void expandColorSet(int L, int index){
        int lastIndex = classifiedColor.get(index).size()-1;
        int assignedColor = classifiedColor.get(index).get(lastIndex).getColor();
        int newColorId = assignedColor + L;
        Color newColor = new Color(newColorId, newColorId);
        classifiedColor.get(index).add(newColor);
    }

    public void saveInterval(){
        try{
            FileOutputStream fileOut = new FileOutputStream("souse/coloredInterval.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(coloredIntervalList);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in souse/coloredInterval.ser");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }


}
