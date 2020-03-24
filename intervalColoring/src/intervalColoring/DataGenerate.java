package intervalColoring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class DataGenerate {

    ArrayList<Interval> intervalSet = new ArrayList<>();
    int number = 0;
    int range = 0;
    Interface Interface = new Interface();

    public DataGenerate() {
    }

    public void dataTypeChoose() {
        Interface.dataChoose();
        int type = Interface.dataType;
        switch (type) {
            case 1:
                randomGenerate();
                break;
            case 2:
                denseGenerate();
                break;
            case 3:
                properGenerate();
                break;
            case 4:
                cliqueGenerate();
                break;
            case 5:
                decreaseGenerate();

        }
        saveInterval();

    }

    public void randomGenerate() {
        Interface.otherData();
        number = Interface.numberOfIntervals;
        range = Interface.lengthOfBottom;
        while (number > 0) {
            int[] coordinate = new int[2];
            int start = (int) (Math.random() * range);
            int end = (int) Math.ceil(Math.random() * (range - start) + start);
            coordinate[0] = start;
            coordinate[1] = end;
            Interval interval = new Interval(coordinate);
            intervalSet.add(interval);
            number--;
        }
        for (int i = 0; i < intervalSet.size(); i++) {
            System.out.println(intervalSet.get(i).getCoordinate()[0] + " " + intervalSet.get(i).getCoordinate()[1]);
        }
    }

    public void denseGenerate() {
        Interface.denseData();
        number = Interface.numberOfIntervals;
        range = Interface.lengthOfBottom;
        int probability = Interface.probability;
        int[] coordinate = new int[2];
        int start = (int) (Math.random() * range);
        int end = (int) Math.ceil(Math.random() * (range - start) + start);
        coordinate[0] = start;
        coordinate[1] = end;
        Interval interval = new Interval(coordinate);
        intervalSet.add(interval);
        int mid = start + (end - start) / 2;
        number--;

        while (number > 0) {
            int event = (int) (Math.random() * 100);
            int[] termCoordinate = new int[2];
            if (event <= probability) {
                int newStart = (int) (Math.random() * mid);
                int newEnd = (int) (Math.random() * (range - mid) + mid);
                termCoordinate[0] = newStart;
                termCoordinate[1] = newEnd;
                Interval termInterval = new Interval(termCoordinate);
                intervalSet.add(termInterval);
                number--;
            } else {
                int newStart = (int) (Math.random() * start);
                int newEnd = (int) Math.ceil(Math.random() * (start - newStart) + newStart);
                termCoordinate[0] = newStart;
                termCoordinate[1] = newEnd;
                Interval termInterval = new Interval(termCoordinate);
                intervalSet.add(termInterval);
                number--;
            }
        }
        for (int i = 0; i < intervalSet.size(); i++) {
            System.out.println(intervalSet.get(i).getCoordinate()[0] + " " + intervalSet.get(i).getCoordinate()[1]);
        }
    }

    public void properGenerate() {
        Interface.properData();
        range = Interface.lengthOfBottom;
        int term = (int) (Math.random() * range) + 1;
        System.out.println(term);
        int index = 0;
        int[] coordinate = new int[2];
        coordinate[0] = 0;
        coordinate[1] = range;
        Interval interval = new Interval(coordinate);
        intervalSet.add(interval);
        Set<Integer> termSet = new HashSet<>();
        while (termSet.size() == term - 1 ? false : true) {
            int num = (int) (Math.random() * range + 1);
            termSet.add(num);
        }
        Iterator<Integer> it = termSet.iterator();
        ArrayList<Integer> sortedList = new ArrayList<>();
        while (it.hasNext()) {
            sortedList.add(it.next());
        }
        Collections.sort(sortedList);
        ArrayList<Integer> secondLine = new ArrayList<>();
        for (int i = 0; i < term; i++) {
            if (i == 0) {
                secondLine.add(sortedList.get(i));
            } else if (i == term - 1) {
                secondLine.add(range - sortedList.get(i - 1));
            } else {
                secondLine.add(sortedList.get(i) - sortedList.get(i - 1));
            }
        }
        for (int i = 0; i < term; i++) {
            int length = secondLine.get(i);
            int distance = 0;
            for (int k = 0; k < i; k++) {
                distance += secondLine.get(k);
            }
            if (length <= 2) {
                if (length != 0) {
                    int[] termCoordinate = new int[2];
                    termCoordinate[0] = distance;
                    termCoordinate[1] = length + distance;
                    Interval termInterval = new Interval(termCoordinate);
                    intervalSet.add(termInterval);
                    index++;
                }
            } else {
                int layer = length / 2;
                for (int j = 0; j < layer; j++) {
                    int[] termCoordinate = new int[2];
                    int preStart = intervalSet.get(index).getCoordinate()[0];
                    int preEnd = intervalSet.get(index).getCoordinate()[1];
                    if (j == 0) {
                        termCoordinate[0] = 1 + distance;
                        termCoordinate[1] = length - 1 + distance;
                        Interval termInterval = new Interval(termCoordinate);
                        intervalSet.add(termInterval);
                        index++;
                    } else {
                        termCoordinate[0] = preStart + 1;
                        termCoordinate[1] = preEnd - 1;
                        if (termCoordinate[0] == termCoordinate[1]) {
                            continue;
                        } else {
                            Interval termInterval = new Interval(termCoordinate);
                            intervalSet.add(termInterval);
                            index++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < intervalSet.size(); i++) {
            System.out.println(intervalSet.get(i).getCoordinate()[0] + " " + intervalSet.get(i).getCoordinate()[1]);
        }
    }

    public void cliqueGenerate() {
        Interface.otherData();
        number = Interface.numberOfIntervals;
        range = Interface.lengthOfBottom;
        int index = -1;
        boolean direction = false;
        while (number > 0) {
            if (intervalSet.size() == 0) {
                int[] coordinate = new int[2];
                int start = (int) (Math.random() * range);
                int end = 0;
                if (start >= range / 2) {
                    end = (int) Math.ceil((Math.random() * (range - start) + start));
                } else {
                    end = (int) Math.ceil((Math.random() * (range / 2 - start) + start));
                }

                coordinate[0] = start;
                coordinate[1] = end;
                Interval interval = new Interval(coordinate);
                intervalSet.add(interval);
                number--;
                index++;
            } else {
                int preStart = intervalSet.get(index).getCoordinate()[0];
                int preEnd = intervalSet.get(index).getCoordinate()[1];
                int[] coordinate = new int[2];
                if (index == 0) {
                    if (preStart >= range / 2) {
                        int end = preStart + 1;
                        int start = end - (int) Math.ceil((Math.random() * end));
                        coordinate[0] = start;
                        coordinate[1] = end;
                        direction = true;
                    } else {
                        int start = preEnd - 1;
                        int end = (int) Math.ceil((Math.random() * (range - start) + start));
                        coordinate[0] = start;
                        coordinate[1] = end;
                        direction = false;
                    }
                } else {
                    if (direction) {
                        if ((index & 1) != 1) {
                            int end = preStart + 1;
                            int start = end - (int) Math.ceil((Math.random() * end));
                            coordinate[0] = start;
                            coordinate[1] = end;
                        } else {
                            int start = preEnd - 1;
                            int end = (int) Math.ceil((Math.random() * (range - start) + start));
                            coordinate[0] = start;
                            coordinate[1] = end;
                        }
                    } else {
                        if ((index & 1) == 1) {
                            int end = preStart + 1;
                            int start = end - (int) Math.ceil((Math.random() * end));
                            coordinate[0] = start;
                            coordinate[1] = end;
                        } else {
                            int start = preEnd - 1;
                            int end = (int) Math.ceil((Math.random() * (range - start) + start));
                            coordinate[0] = start;
                            coordinate[1] = end;
                        }
                    }
                }
                Interval interval = new Interval(coordinate);
                intervalSet.add(interval);
                number--;
                index++;
            }
        }
        for (int i = 0; i < intervalSet.size(); i++) {
            System.out.println(intervalSet.get(i).getCoordinate()[0] + " " + intervalSet.get(i).getCoordinate()[1]);
        }

    }

    public void decreaseGenerate(){
        Interface.decreaseData();
        range = Interface.lengthOfBottom;
        number= (int)Math.ceil(range/2);
        int start = 0;
        int end = range;

        while(number>0){
            int[] coordinate = new int[2];
            coordinate[0] = start;
            coordinate[1] = end;
            Interval interval = new Interval(coordinate);
            intervalSet.add(interval);
            start++;
            end--;

            number--;
        }
        Collections.reverse(intervalSet);
        for (int i = 0; i < intervalSet.size(); i++) {
            System.out.println(intervalSet.get(i).getCoordinate()[0] + " " + intervalSet.get(i).getCoordinate()[1]);
        }

    }

    public void saveInterval(){
        try{
            FileOutputStream fileOut = new FileOutputStream("souse/Interval.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(intervalSet);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in souse/Interval.ser");
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
