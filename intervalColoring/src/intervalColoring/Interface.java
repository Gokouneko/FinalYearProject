package intervalColoring;

import java.util.Scanner;

public class Interface {

    int lengthOfBottom = 0;
    int numberOfIntervals = 0;
    int probability = 0;
    int dataType = 0;
    int algoirthmType = 0;
    int change = 0;

    public Interface() {
    }

    public void dataChoose(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Choose a type of interval");
        System.out.println("1. Random     2. Dense     3. Proper     4. Clique");
        dataType = keyboard.nextInt();
    }

    public void otherData(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("The length of bottom line:");
        lengthOfBottom = keyboard.nextInt();
        System.out.println("The number of intervals:");
        numberOfIntervals = keyboard.nextInt();

    }

    public void denseData() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("The length of bottom line:");
        lengthOfBottom = keyboard.nextInt();
        System.out.println("The number of intervals:");
        numberOfIntervals = keyboard.nextInt();
        System.out.println("The probability of dense intervals:");
        probability = keyboard.nextInt();
    }

    public void properData(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("The length of bottom line:");
        lengthOfBottom = keyboard.nextInt();
    }

    public void algorithmChoose(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Choose a type of algorithm");
        System.out.println("1. Classfy-Greedy     2. Ordinary-Greedy     3. Dynamic-Recolor     4. Assign-Color-Totally");
        algoirthmType = keyboard.nextInt();
    }

    public void chooseLoop(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Do you want to choose another algorithm");
        System.out.println("1. Yes     2. No");
        change = keyboard.nextInt();
    }


}
