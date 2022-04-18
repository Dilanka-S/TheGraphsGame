package edu.curtin.app;

import java.util.*;
import java.io.*;
import java.util.logging.Logger;

public class App {
    private static Logger appLogger = Logger.getLogger(App.class.getName());
    public static void main(String[] args) {
        //System.out.println("Hello world");

        // If you wish to change the name and/or package of the class containing 'main()', you
        // will also need to update the 'mainClass = ...' line in build.gradle.
        try{
            //Welcome message
            System.out.println("WELCOME TO THE");
            System.out.println("\033[31mMAZE \033[32mGAME!\033[m");
            //Allowing the program to be tested with different files through the CLI
            Scanner sc = new Scanner(System.in);
            //String filename;
//            System.out.print("\n> Please enter the name of the metadata file for the map : ");
//            String filename = sc.nextLine();
            Scanner fileScanner = new Scanner(new File("sample.txt"));
            //Reading map size
            int rowNum = fileScanner.nextInt();
            int colNum = fileScanner.nextInt();
            int actCols = (3*rowNum)+(rowNum+1);
            int actRows = ((2*colNum)+1);
            //System.out.println("actual number of rows : "+actRows+"\nactual number of columns : "+actCols);
            Map map = new Map(actRows,actCols);

            //Putting borders
            for (int i = 0; i < actCols; i++) {
                map.setMap(0,i,new HorizontalWall());
            }
            for (int i = 0; i < actCols; i++) {
                map.setMap(actRows-1,i,new HorizontalWall());
            }
            for (int i = 0; i < actRows; i++) {
                map.setMap(i,0, new VerticalWall());
            }
            for (int i = 0; i < actRows; i++) {
                map.setMap(i,actCols-1, new VerticalWall());
            }

            //System.out.println("\nrowNum is : "+rowNum+ "\tcolNum is : " +colNum);
            appLogger.info("Size of the map is : "+rowNum+ " x " +colNum);
            fileScanner.nextLine();

            //String itemCode = "";
            int wvCount=0, whCount=0, dhCount=0, dvCount=0, mCount=0,eCount=0,kCount=0;
            while(fileScanner.hasNext()){
                String line = fileScanner.nextLine();
                String[] splitBy = line.split(" ");
                //System.out.println(line);
                switch(splitBy[0]){
                    case "WV" :
                        //System.out.println("WV");
                        wvCount++;
                        int xWV = Integer.parseInt(splitBy[1]);
                        int yWV = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of Vertical Wall("+wvCount+") is : \n\tRow = "+xWV+" Column = "+yWV);
                        map.setMap(adjustRow(xWV,"WV"),adjustColumn(yWV,"WV"), new VerticalWall());
                        break;
                    case "WH" :
                        //System.out.println("WH");
                        whCount++;
                        int xWH = Integer.parseInt(splitBy[1]);
                        int yWH = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of Horizontal Wall("+whCount+") is : \n\tRow = "+xWH+" Column = "+yWH);
                        map.setMap(adjustRow(xWH,"WH"),(adjustColumn(yWH,"WH")-1), new HorizontalWall());
                        map.setMap(adjustRow(xWH,"WH"),adjustColumn(yWH,"WH"), new HorizontalWall());
                        map.setMap(adjustRow(xWH,"WH"),(adjustColumn(yWH,"WH")+1), new HorizontalWall());
                        break;
                    case "S" :
                        //System.out.println("S");
                        int xS = Integer.parseInt(splitBy[1]);
                        int yS = Integer.parseInt(splitBy[2]);
                        appLogger.info("The Starting Position of the Player is : \n\tRow = "+xS+" Column = "+yS);
                        map.setMap(adjustRow(xS,"S"),adjustColumn(yS,"S"), new Player());
                        break;
                    case "E" :
                        //System.out.println("E");
                        eCount++;
                        int xE = Integer.parseInt(splitBy[1]);
                        int yE = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of End Point("+eCount+") is : \n\tRow = "+xE+" Column = "+yE);
                        map.setMap(adjustRow(xE,"E"),adjustColumn(yE,"E"), new End());
                        break;
                    case "DH" :
                        //System.out.println("DH");
                        dhCount++;
                        int xDH = Integer.parseInt(splitBy[1]);
                        int yDH = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of Horizontal Door("+dhCount+") is : \n\tRow = "+xDH+" Column = "+yDH);
                        break;
                    case "DV" :
                        //System.out.println("DV");
                        dvCount++;
                        int xDV = Integer.parseInt(splitBy[1]);
                        int yDV = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of Vertical Door("+dvCount+") is : \n\tRow = "+xDV+" Column = "+yDV);
                        break;
                    case "K" :
                        //System.out.println("K");
                        int xK = Integer.parseInt(splitBy[1]);
                        int yK = Integer.parseInt(splitBy[2]);
                        String keyColor = keyColorFinder(Integer.parseInt(splitBy[3]));
                        appLogger.info("The position of a "+keyColor+" color Key is : \n\tRow = "+xK+" Column = "+yK);
                        map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new Keys());
                        break;
                    case "M" :
                        mCount++;
                        //System.out.println("M");
                        int xM = Integer.parseInt(splitBy[1]);
                        int yM = Integer.parseInt(splitBy[2]);

                        String message = " ";
//                        for (int i = 3; i < line.length() ; i++) {
//                            //message.append(splitBy[i]);
//                            message = message + splitBy[i]+" ";
//                        }
                        //if(Character.isDigit(line.charAt()))
                        appLogger.info("Position of Message("+mCount+") is : \n\tRow = "+xM+" Column = "+yM+"\n\t"+message);
                        //System.out.println(message);
                        break;
                    default :
                        System.err.println("An input category that was not predefined has been entered." +
                                " Kindly check the metadata input file again!");
                        break;
                }
                //System.out.println(map);

            }
            appLogger.info(" > Total number of Horizontal Walls : "+whCount);
            appLogger.info(" > Total number of Vertical Walls : "+wvCount);
            appLogger.info(" > Total number of Horizontal Doors : "+dhCount);
            appLogger.info(" > Total number of Vertical Doors : "+dvCount);
            appLogger.info(" > Total number of Messages : "+mCount);
            appLogger.info(" > Total number of Keys : "+kCount);

            System.out.println(map.display());

            while(true){
                if(map.winCondition()==true){
                    System.out.println("\033[2J");
                    System.out.println("\033[31mYOU WIN!!");
                    System.out.println("\nThanks for playing the maze game.\nCreated By\t:\tD.V.Seneviratne" +
                            "\nStudent ID\t:\t20529624 " +
                            "\nInstitute\t:\tCurtin University/SLIIT International - Sri Lanka ");
                    break;
                }else{
                    System.out.println("Please entire your desired move or type 'exit' to close the game");
                    System.out.println("Action : ");
                    String input = sc.next();
                    if(input.equals("exit")){
                        System.out.println("\nThanks for playing the maze game.\nCreated By\t:\tD.V.Seneviratne" +
                                "\nStudent ID\t:\t20529624 " +
                                "\nInstitute\t:\tCurtin University/SLIIT International - Sri Lanka ");
                        break;
                    }
                    map.move(input);
                    System.out.println("\033[2J");
                    System.out.println(map.display());
                }

            }
            //System.out.println("* while loop finished");
            sc.close();
            fileScanner.close();
//      }catch (NullPointerException exception){
//            System.err.println("An error has occurred : " +exception.getMessage());

        }catch(Exception e){
            System.err.println("An error has occurred : "+e.getMessage());
        }
    }
    private static String keyColorFinder(int colorCode){
        //An enhanced switch case suggested by IntelliJ
        return switch (colorCode) {
            case 1 -> "Red";
            case 2 -> "Green";
            case 3 -> "Yellow";
            case 4 -> "Blue";
            case 5 -> "Magenta";
            case 6 -> "Cyan";
            default -> null;
        };
    }
    private static int adjustRow(int row,String type){
        int adjustedRow=0;
        if(type.equals("WH")){
            adjustedRow = (2*row)+1;
        }else{
            if(row==0){
                adjustedRow=1;
            }else if(row>0){
                adjustedRow = row + 2;
            }
        }
        return adjustedRow;
    }
    private static int adjustColumn(int col, String type){
        int adjustedCol=0;
        if(type.equals("WV")){
            adjustedCol = col*4;
        }else
//        if(col==0){
//            adjustedCol=0;
//        }else
        if(col>=0){
            adjustedCol = (4*col)+2;
        }
        return adjustedCol;
    }
}
