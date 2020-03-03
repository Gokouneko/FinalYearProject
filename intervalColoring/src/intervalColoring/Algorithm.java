package intervalColoring;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Algorithm {
    DataGenerate dataGenerate = new DataGenerate();
    Interface Interface = new Interface();
    int range;
    ArrayList<ArrayList<Color>> classifiedColor = new ArrayList<>();
    ArrayList<ArrayList<Integer>> classifiedColorId = new ArrayList<>();
    ArrayList<ColoredInterval> coloredIntervalList = new ArrayList<>();
    ArrayList<ArrayList<ColoredInterval>> classifiedcoloredIntervalList = new ArrayList<>();

    public Algorithm() {
    }

    public void algorithmChoose(){
        dataGenerate.dataTypeChoose();
        Interface.algorithmChoose();
        int type = Interface.algoirthmType;
        switch (type){
            case 1:
                classfyGreedy();
                break;
            case 2:
                ordinaryGreedy();
                break;
            case 3:
                dynamicRecolor();
                break;
            case 4:

                break;
        }
    }

    public void classfyGreedy() {
        range = dataGenerate.range;
        double lMax = dataGenerate.range;
        double lMin = 1;
        int L = 1 + (int) Math.ceil(Math.log(lMax / lMin) / Math.log(2));
        ArrayList<Interval> uncoloredList = readInterval();
        ArrayList<Color> colorSet = initColorSet(uncoloredList);

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
                    if (isOverlapping(uncoloredInterval, classifiedColor.get(L-i-1))) {
                        if (classifiedColorId.get(i).size() == 0) {
                            index = 0;
                        } else {
                            index = classifiedColorId.get(i).get(classifiedColorId.get(i).size()-1)+1;
                        }
                        try{
                            color = classifiedColor.get(L-i-1).get(index);
                        }catch(Exception e){
                            expandColorSet(L, L-i-1);
                            color = classifiedColor.get(L-i-1).get(index);
                            if(color.getColor()>=classifiedcoloredIntervalList.size()){
                                int term = color.getColor()-classifiedcoloredIntervalList.size();
                                for(int j=0;j<=term;j++){
                                    ArrayList<ColoredInterval> sameColor = new ArrayList<>();
                                    classifiedcoloredIntervalList.add(sameColor);
                                }
                            }
                        }
                        classifiedColorId.get(i).add(index);

                    } else {
                        ArrayList<ColoredInterval> notOverlappingList = getNotOverlappingList(uncoloredInterval, classifiedColor.get(L-i-1));
                        ArrayList<Integer> colorId = new ArrayList<>();
                        /* Find all the possible color the interval can be assigned */
                        for (ColoredInterval termInterval : notOverlappingList) {
                            colorId.add(termInterval.getColor().getColor());
                        }
                        /* Give the interval a smallest cost color it can be assigned */
                        index = Collections.min(colorId);
                        color = colorSet.get(index);

                    }
                    ColoredInterval coloredInterval = new ColoredInterval(uncoloredInterval, color);
                    classifiedcoloredIntervalList.get(index).add(coloredInterval);
                    coloredIntervalList.add(coloredInterval);
                }
            }
        }
        saveInterval();
    }

    public void ordinaryGreedy(){
        range = dataGenerate.range;
        ArrayList<Interval> uncoloredList = readInterval();
        ArrayList<Color> colorSet = initColorSet(uncoloredList);
        ArrayList<Integer> coloredId = new ArrayList<>();
        for(Interval uncoloredInterval: uncoloredList){
            int index;
            Color color;
            if(isOverlapping(uncoloredInterval, colorSet)){
                if(coloredId.size()==0){
                    index = 0;
                }else{
                    index = coloredId.get(coloredId.size()-1)+1;
                }
                color = colorSet.get(index);
                coloredId.add(index);
            }else{
                ArrayList<ColoredInterval> notOverlappingList = getNotOverlappingList(uncoloredInterval, colorSet);
                ArrayList<Integer> colorId = new ArrayList<>();
                /* Find all the possible color the interval can be assigned */
                for (ColoredInterval termInterval : notOverlappingList) {
                    colorId.add(termInterval.getColor().getColor());
                }
                /* Give the interval a smallest cost color it can be assigned */
                index = Collections.min(colorId);
                color = colorSet.get(index);

            }

            ColoredInterval coloredInterval = new ColoredInterval(uncoloredInterval, color);
            classifiedcoloredIntervalList.get(index).add(coloredInterval);
            coloredIntervalList.add(coloredInterval);
        }
        saveInterval();
    }

    public void dynamicRecolor(){
        range = dataGenerate.range;
        ArrayList<Interval> uncoloredList = readInterval();
        ArrayList<Color> colorSet = initColorSet(uncoloredList);
        ArrayList<Integer> coloredId = new ArrayList<>();
        for(Interval uncoloredInterval: uncoloredList){
            int index;
            Color color;
            if(isOverlapping(uncoloredInterval, colorSet)){
                if(coloredId.size()==0){
                    index = 0;
                }else{
                    index = coloredId.get(coloredId.size()-1)+1;
                }
                color = colorSet.get(index);
                coloredId.add(index);

            }else{
                ArrayList<ArrayList<ColoredInterval>> notOverlappingSet = getNotOverlappingIntervalSet(uncoloredInterval, colorSet);
                ArrayList<Integer>colorIdSet = new ArrayList<>();
                for(ArrayList<ColoredInterval> termList : notOverlappingSet){
                    colorIdSet.add(termList.get(0).getColor().getColor());
                }
                index= Collections.min(colorIdSet);
                color = colorSet.get(index);

            }
            ColoredInterval coloredInterval = new ColoredInterval(uncoloredInterval, color);
            classifiedcoloredIntervalList.get(index).add(coloredInterval);

            shiftColor(index, colorSet);

        }
        for (int i=0;i<classifiedcoloredIntervalList.size();i++) {
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(i)) {
                coloredIntervalList.add(termInterval);
            }

        }
        saveInterval();
    }

    public void shiftColor(int index, ArrayList<Color> colorSet){
        int length = getTotalLength(classifiedcoloredIntervalList.get(index));
        boolean haveShifted = false;
        for(int i=0;i<index;i++){
            int lengthOfColoredIntervals = getTotalLength(classifiedcoloredIntervalList.get(i));
            if(length>lengthOfColoredIntervals){
                int indexOfColoredInterval = classifiedcoloredIntervalList.get(i).get(0).getColor().getColor();
                Color color = colorSet.get(indexOfColoredInterval);
                for(int j=0;j<classifiedcoloredIntervalList.get(index).size();j++){
                    classifiedcoloredIntervalList.get(index).get(j).setColor(color);
                }
                Color currentColor = colorSet.get(index);
                for(int j=0;j<classifiedcoloredIntervalList.get(i).size();j++){
                    classifiedcoloredIntervalList.get(i).get(j).setColor(currentColor);
                }
                Collections.swap(classifiedcoloredIntervalList,i,index);
                haveShifted = true;
                break;
            }
        }
        if(haveShifted){
            shiftColor(index, colorSet);
        }
    }

    public Integer getTotalLength(ArrayList<ColoredInterval>termList){
        int length = 0;
        for (ColoredInterval termInterval: termList){
            length+=termInterval.getLength();
        }
        return length;
    }


    public ArrayList<Color> initColorSet(ArrayList<Interval> uncoloredList){
        ArrayList<Color> colorSet = new ArrayList<>();
        for (int i = 0; i < uncoloredList.size(); i++) {
            ArrayList<ColoredInterval> sameColor = new ArrayList<>();
            Color color = new Color(i, i);
            colorSet.add(color);
            classifiedcoloredIntervalList.add(sameColor);
        }
        return colorSet;
    }

    public boolean isOverlapping(Interval interval, ArrayList<Color>colorSet) {
        boolean overlapping = true;
        int[] coordinate = interval.getCoordinate();
        for (Color termColor : colorSet) {
            int colorId = termColor.getColor();
            ArrayList<Integer> termList = new ArrayList<>();
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                /* Judge if there is an interval that not overlapping with the current interval */
                if (start >= coordinate[1] || end <= coordinate[0]) {
                    termList.add(1);
                }
            }
            if(termList.size()==classifiedcoloredIntervalList.get(colorId).size()&&termList.size()!=0){
                overlapping = false;
                break;
            }
        }
        return overlapping;
    }

    public ArrayList<ColoredInterval> getNotOverlappingList(Interval interval, ArrayList<Color>colorSet) {
        ArrayList<ColoredInterval> notOverlappingList = new ArrayList<>();
        int[] coordinate = interval.getCoordinate();
        for (Color termColor : colorSet) {
            int colorId = termColor.getColor();
            ArrayList<Integer> termList = new ArrayList<>();
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                /* Find interval that not overlapping with the current interval */
                if (start >= coordinate[1] || end <= coordinate[0]) {
                    termList.add(1);

                }
            }
            if(termList.size()==classifiedcoloredIntervalList.get(colorId).size()&&termList.size()!=0){
                for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                    notOverlappingList.add(termInterval);
                }

            }
        }
        return notOverlappingList;
    }

    public ArrayList<ArrayList<ColoredInterval>>getNotOverlappingIntervalSet(Interval interval, ArrayList<Color>colorSet){
        ArrayList<ArrayList<ColoredInterval>> notOverlappingSet = new ArrayList<>();
        int[] coordinate = interval.getCoordinate();
        for (Color termColor : colorSet) {
            int colorId = termColor.getColor();
            ArrayList<Integer> termList = new ArrayList<>();
            for (ColoredInterval termInterval : classifiedcoloredIntervalList.get(colorId)) {
                int start = termInterval.getInterval().getCoordinate()[0];
                int end = termInterval.getInterval().getCoordinate()[1];
                /* Find interval that not overlapping with the current interval */
                if (start >= coordinate[1] || end <= coordinate[0]) {
                    termList.add(1);

                }
            }
            if(termList.size()==classifiedcoloredIntervalList.get(colorId).size()&&termList.size()!=0){
                notOverlappingSet.add(classifiedcoloredIntervalList.get(colorId));

            }
        }
        return notOverlappingSet;
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
            System.out.println("Serialized data is saved in souse/coloredInterval.ser");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }


}
