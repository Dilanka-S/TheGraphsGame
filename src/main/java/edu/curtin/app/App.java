package edu.curtin.app;

import edu.curtin.app.Exceptions.MazeException;
import edu.curtin.app.Model.*;
import edu.curtin.app.Model.Borders.leftBotBorder;
import edu.curtin.app.Model.Borders.leftTopBorder;
import edu.curtin.app.Model.Borders.rightBotBorder;
import edu.curtin.app.Model.Borders.rightTopBorder;
import edu.curtin.app.Model.Doors.HorizontalDoor;
import edu.curtin.app.Model.Doors.VerticalDoor;
import edu.curtin.app.Model.Keys.*;
import edu.curtin.app.Model.Map;
import edu.curtin.app.Model.Walls.HorizontalWall;
import edu.curtin.app.Model.Walls.VerticalWall;

import java.util.*;
import java.io.*;
import java.util.logging.Logger;

public class App {
    private static Logger appLogger = Logger.getLogger(App.class.getName());
    public static void main(String[] args) {
        try{
            //Welcome message
            System.out.println("+ -------------------- +");
            System.out.println("|       WELCOME TO     |");
            System.out.println("|          THE         |");
            System.out.println("|       \033[31mMAZE \033[32mGAME!\033[m     |");
            System.out.println("+ -------------------- +\n");

            //Allowing the program to be tested with different files through the CLI
            Scanner sc = new Scanner(System.in);
            boolean menuBool = true;
            while(menuBool==true){
                System.out.println("""
                        Kindly choose one of the below menu options
                        \t1. Instructions
                        \t2. Play the Maze Game
                        \t3. Exit""");
                System.out.print("Your selection : ");
                int menuSelection = sc.nextInt();
                switch (menuSelection){
                    case 1 :
                        instructionsBanner();
                        break;
                    case 2 :
                        playGame();
                        menuBool = false;
                        break;
                    case 3 :
                        exitBanner();
                        menuBool = false;
                        break;
                    default:
                        System.err.println("Incorrect Menu Selection");
                        break;
                }

            }

        } catch (Exception mainExceptions){
            System.err.println("An error has occurred when running the game : "+mainExceptions.getMessage());
        }
        //System.out.println("Hello world");

        // If you wish to change the name and/or package of the class containing 'main()', you
        // will also need to update the 'mainClass = ...' line in build.gradle.

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
        if(type.equals("WH") || (type.equals("DH"))){
            adjustedRow = (2*row);
        }else if(type.equals("WV") || (type.equals("DV"))) {
            adjustedRow = (2*row)+1;
        } else{
                if(row==0){
                    adjustedRow=1;
                }else if(row>0){
                    adjustedRow = row + 3;
                }
        }

        return adjustedRow;
    }
    private static int adjustColumn(int col, String type){
        int adjustedCol=0;
        if((type.equals("WV") || (type.equals("DV")))){
            adjustedCol = col*4;
        }else if(col>=0){
            adjustedCol = (4*col)+2;
        }
        return adjustedCol;
    }
    private static Map insertBorders(Map map,int actCols,int actRows){
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
        map.setMap(0,0,new leftTopBorder());
        map.setMap(0,actCols-1,new rightTopBorder());
        map.setMap(actRows-1,0,new leftBotBorder());
        map.setMap(actRows-1,actCols-1,new rightBotBorder());
        map.setIntersections(actRows,actCols);

        return map;
    }
    private static void exitBanner(){
        System.out.println("""

                Thanks for playing the maze game.
                Created By\t:\tD.V.Seneviratne
                Student ID\t:\t20529624\s
                Institute\t:\tCurtin University/SLIIT International - Sri Lanka\s""");
    }
    public static void playGame(){
        try{
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
            edu.curtin.app.Model.Map map = new Map(actRows,actCols);

            //Putting borders
            //map = insertBorders(map,actCols,actRows);

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
                        map.setMap(adjustRow(xDH,"DH"),adjustColumn(yDH,"DH")-1, new HorizontalDoor());
                        map.setMap(adjustRow(xDH,"DH"),adjustColumn(yDH,"DH"), new HorizontalDoor());
                        map.setMap(adjustRow(xDH,"DH"),adjustColumn(yDH,"DH")+1, new HorizontalDoor());
                        break;
                    case "DV" :
                        //System.out.println("DV");
                        dvCount++;
                        int xDV = Integer.parseInt(splitBy[1]);
                        int yDV = Integer.parseInt(splitBy[2]);
                        appLogger.info("Position of Vertical Door("+dvCount+") is : \n\tRow = "+xDV+" Column = "+yDV);
                        map.setMap(adjustRow(xDV,"DV"),adjustColumn(yDV,"DV"), new VerticalDoor());
                        break;
                    case "K" :
                        //System.out.println("K");
                        int xK = Integer.parseInt(splitBy[1]);
                        int yK = Integer.parseInt(splitBy[2]);
                        String keyColor = keyColorFinder(Integer.parseInt(splitBy[3]));
                        appLogger.info("The position of added "+keyColor+" color Key is : \n\tRow = "+xK+" Column = "+yK);
                        switch (Integer.parseInt(splitBy[3])){
                            case 1 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new RedKey());
                                break;
                            case 2 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new GreenKey());
                                break;
                            case 3 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new YellowKey());
                                break;
                            case 4 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new BlueKey());
                                break;
                            case 5 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new MagentaKey());
                                break;
                            case 6 :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new CyanKey());
                                break;
                            default :
                                map.setMap(adjustRow(xK,"K"),adjustColumn(yK,"K"), new Keys());
                                //System.err.println("Incorrect key type entered");
                                break;
                        }
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

            //System.out.println("\033[2J");
            map = insertBorders(map,actCols,actRows);
            System.out.println(map.display());

            while(true){
                if(map.winCondition()==true){
                    System.out.println("\033[2J");
                    System.out.println("\t\t\033[31mYOU W0N!");
                    exitBanner();
                    break;
                }else{
                    System.out.println("Please entire your desired move or type 'exit' to close the game");
                    System.out.print("Action : ");
                    String input = sc.next();
                    if(input.equals("exit")){
                        exitBanner();
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

        }catch (MazeException mazeException){
            System.err.println(mazeException.getMessage());
        }
        catch(Exception e){
            System.err.println("An error has occurred : "+e.getMessage());
        }
    }
    private static void instructionsBanner(){
        System.out.println("\033[2J");
        System.out.println("""
                The following are the controls to move the '(P)layer' position in the map
                \t'n'\t-\t To move NORTH (up)
                \t's'\t-\t To move SOUTH (down)
                \t'e'\t-\t To move EAST (left)
                \t'n'\t-\t To move WEST (right)""");
        System.out.println("""
                Here is the Legend for the map
                \tP\t-\t Player's current location
                \tE\t-\t END/win location(s)
                \t\u2555\t-\t Keys necessary to open the corresponding color door
                \t\u2592\t-\t Vertical door that can be opened with the corresponding color key
                \t\u2592\u2592\u2592\t-\t Horizontal door that can be opened with the corresponding color key
                
                """);

    }
}
