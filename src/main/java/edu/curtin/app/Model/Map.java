package edu.curtin.app.Model;

import edu.curtin.app.Exceptions.DoorAccessException;
import edu.curtin.app.Exceptions.HitHorizontalWall;
import edu.curtin.app.Exceptions.HitVerticalWall;
import edu.curtin.app.Exceptions.MazeException;
import edu.curtin.app.Model.Borders.*;
import edu.curtin.app.Model.Doors.HorizontalDoor;
import edu.curtin.app.Model.Doors.VerticalDoor;
import edu.curtin.app.Model.Keys.Keys;
import edu.curtin.app.Model.Walls.HorizontalWall;
import edu.curtin.app.Model.Walls.VerticalWall;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Map {
    public Cell[][] map;
    public ArrayList<String> keyList = new ArrayList<>();
    public ArrayList<String> warningList = new ArrayList<>();
    private int playerRow;
    private int playerColumn;
    private int endColumn;
    private int endRow;
    private final int actualRows;
    private final int actualCols;
    private static final Logger logger = Logger.getLogger(Map.class.getName());

    public Map(int rows, int cols){
        map = new Cell[rows][cols];
        actualRows = rows;
        actualCols = cols;
    }
    public void setMap(int x,int y, Cell cellObj){
        map[x][y] = cellObj;
        if (cellObj instanceof Player){
            playerRow = x;
            playerColumn = y;
        }else if (cellObj instanceof End){
            endRow = x;
            endColumn = y;
        }
    }
    public void setIntersections(int actualRows,int actualCols){
        for (int i = 4; i < actualCols-1 ; i+=4) {
            if(map[1][i] instanceof VerticalWall){
                setMap(0,i,new topIntersection());
            }
            if(map[actualRows-2][i] instanceof VerticalWall){
                setMap(actualRows-1,i, new botIntersection());
            }
        }
        for (int i = 2; i < actualRows-1; i+=2) {
            if(map[i][1] instanceof HorizontalWall){
                setMap(i,0, new leftIntersection());
                //System.out.println("Left Intersection");
            }
            if(map[i][actualRows-2] instanceof HorizontalWall){
                setMap(i,actualRows-1, new rightIntersection());
            }
        }
    }
    public void setMidIntersections(){
        for (int i = 1; i < actualRows-2 ; i+=2) {
            for (int j = 4; j < actualCols-1; j+=4) {
                if(map[i][j] instanceof VerticalWall){
                    if(map[i+1][j+1] instanceof HorizontalWall){
                        setMap(i+1,j,new rightMidIntersection());
                    }
                    if(map[i+2][j] instanceof VerticalWall){
                        setMap(i+1,j, new VerticalWall());
                    }
                }
            }
        }
        for (int i = 2; i < actualRows-2 ; i+=2) {
            for (int j = 2; j < actualCols ; j+=4) {
                if(map[i][j] instanceof HorizontalWall){
                    if(map[i+1][j+2] instanceof VerticalWall){
                        setMap(i,j+2, new rightMidDownIntersection());
                    }else if(map[i+1][j-2]instanceof VerticalWall){
                        setMap(i,j-2, new leftTopBorder());
                    }if(map[i+1][j+2] instanceof VerticalDoor){
                        setMap(i,j+2, new rightMidDownIntersection());
                    }if(map[i-1][j+2] instanceof VerticalWall){
                        setMap(i,j+2, new rightBotBorder());
                    }if(map[i-1][j-2] instanceof VerticalDoor){
                        setMap(i,j-2, new leftBotBorder());
                    }
                }

            }
        }

    }
   // @Override
    public String display(){
        //System.out.println("map length is : "+map.length+"\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < actualRows; i++) {
            for (int j = 0; j < actualCols; j++) {
                if(map[i][j]==null){
                    builder.append(" ");
                }else{
                    builder.append(map[i][j]);
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    public void move(String input) throws HitVerticalWall, HitHorizontalWall {
        try{
            switch (input){
                case "n" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                        warningList.add("\033[31mPlayer has moved outside map\033[m");
                    }else if(map[playerRow-1][playerColumn] instanceof HorizontalWall){
                        warningList.add("\033[31mYou bumped into a Horizontal Wall next to you\033[m!");
                        throw new HitHorizontalWall("You bumped into a Horizontal Wall next to you!");
                    }else if(map[playerRow-2][playerColumn] instanceof Keys){
                        String key = String.valueOf(map[playerRow-2][playerColumn]);
                        //System.out.println(keyCodeFinder(key));
                        key = keyCodeFinder(key);
                        //System.out.println(key);
                        keysList(key);
                        System.out.println("You have obtained a "+key+" key");
                        warningList.add("\033[32mYou have obtained a "+key+" key\033[m");
                        logger.info("Player has obtained a "+key+" key at ["+(playerRow-2)+","+playerColumn+"]");
                    }else if(map[playerRow-1][playerColumn] instanceof HorizontalDoor){
                        //System.out.println("Door is there");
                        String door = String.valueOf(map[playerRow-1][playerColumn]);
                        door = doorCodeFinder(door);
                        if(keyList.contains(door)){
                            System.out.println("You opened the "+door+" door with the "+door+" key you had!");
                            warningList.add("\033[33mYou opened the "+door+" door with the "+door+" key you had!\033[m");
                        }else{
                            warningList.add("\033[31mYou don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue\033[m");
                            throw new DoorAccessException("You don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue ");
                        }
                    }
                    map[playerRow-2][playerColumn] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved NORTH from ["+playerRow+","+playerColumn+"] to ["+(playerRow-2)+","+playerColumn+"]");
                    playerRow = playerRow-2;
                    break;
                case "s" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                        warningList.add("\033[31mPlayer has moved outside map\033[m");
                    } else if(map[playerRow+1][playerColumn] instanceof HorizontalWall){
                        warningList.add("\033[31mYou bumped into a Horizontal Wall next to you\033[m!");
                        throw new HitHorizontalWall("You bumped into a Horizontal Wall next to you!");
                    } else if(map[playerRow+2][playerColumn] instanceof Keys){
                        String key = String.valueOf(map[playerRow+2][playerColumn]);
                        key = keyCodeFinder(key);
                        keysList(key);
                        System.out.println("You have obtained a "+key+" key");
                        warningList.add("\033[32mYou have obtained a "+key+" key\033[m");
                        logger.info("Player has obtained a "+key+" key at ["+(playerRow+2)+","+playerColumn+"]");
                    }else if(map[playerRow+1][playerColumn] instanceof HorizontalDoor){
                        //System.out.println("Door is there");
                        String door = String.valueOf(map[playerRow+1][playerColumn]);
                        door = doorCodeFinder(door);
                        if(keyList.contains(door)){
                            System.out.println("You opened the "+door+" door with the "+door+" key you had!");
                            warningList.add("\033[33mYou opened the "+door+" door with the "+door+" key you had!\033[m");
                        }else{
                            warningList.add("\033[31mYou don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue\033[m");
                            throw new DoorAccessException("You don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue ");
                        }
                    }
                    map[playerRow+2][playerColumn] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved SOUTH from ["+playerRow+","+playerColumn+"] to ["+(playerRow+2)+","+playerColumn+"]");
                    playerRow = playerRow+2;
                    break;
                case "w" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                        warningList.add("\033[31mPlayer has moved outside map\033[m");
                    } else if(map[playerRow][playerColumn-2] instanceof VerticalWall){
                        warningList.add("\033[31mYou bumped into a Vertical Wall next to you\033[m!");
                        throw new HitVerticalWall("You bumped into a Vertical Wall next to you!");
                    } else if(map[playerRow][playerColumn-4] instanceof Keys){
                        String key = String.valueOf(map[playerRow][playerColumn-4]);
                        key = keyCodeFinder(key);
                        keysList(key);
                        System.out.println("You have obtained a "+key+" key");
                        warningList.add("\033[32mYou have obtained a "+key+" key\033[m");
                        logger.info("Player has obtained a "+key+" key at ["+playerRow+","+(playerColumn-4)+"]");
                    }else if(map[playerRow][playerColumn-2] instanceof VerticalDoor){
                        //System.out.println("Door is there");
                        String door = String.valueOf(map[playerRow][playerColumn-2]);
                        door = doorCodeFinder(door);
                        if(keyList.contains(door)){
                            warningList.add("\033[33mYou opened the "+door+" door with the "+door+" key you had!\033[m");
                            System.out.println("You opened the "+door+" door with the "+door+" key you had!");
                        }else{
                            warningList.add("\033[31mYou don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue\033[m");
                            throw new DoorAccessException("You don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue ");
                        }
                    }
                    map[playerRow][playerColumn-4] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved WEST from ["+playerRow+","+playerColumn+"] to ["+playerRow+","+(playerColumn-4)+"]");
                    playerColumn = playerColumn-4;
                    break;
                case "e" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                        warningList.add("\033[31mPlayer has moved outside map\033[m");
                    } else if(map[playerRow][playerColumn+2] instanceof VerticalWall){
                        warningList.add("\033[31mYou bumped into a Vertical Wall next to you\033[m!");
                        throw new HitVerticalWall("You bumped into a Vertical Wall next to you!");
                    } else if(map[playerRow][playerColumn+4] instanceof Keys){
                        String key = String.valueOf(map[playerRow][playerColumn+4]);
                        key = keyCodeFinder(key);
                        keysList(key);
                        System.out.println("You have obtained a "+key+" key");
                        warningList.add("\033[32mYou have obtained a "+key+" key\033[m");
                        logger.info("Player has obtained a "+key+" key at ["+playerRow+","+(playerColumn+4)+"]");
                    }else if(map[playerRow][playerColumn+2] instanceof VerticalDoor){
                        //System.out.println("Door is there");
                        String door = String.valueOf(map[playerRow][playerColumn+2]);
                        door = doorCodeFinder(door);
                        if(keyList.contains(door)){
                            warningList.add("\033[33mYou opened the "+door+" door with the "+door+" key you had!\033[m");
                            System.out.println("You opened the "+door+" door with the "+door+" key you had!");
                        }else{
                            warningList.add("\033[31mYou don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue\033[m");
                            throw new DoorAccessException("You don't have access to this "+door+" door. Please obtain a " +
                                    ""+door+" colored key to continue ");
                        }
                    }
                    map[playerRow][playerColumn+4] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved EAST from ["+playerRow+","+playerColumn+"] to ["+playerRow+","+(playerColumn+4)+"]");
                    playerColumn = playerColumn+4;
                    break;
                default:
                    System.err.println("Invalid move");
                    break;

            }
        }catch (MazeException mazeException){
            System.out.println("Invalid move : "+mazeException.getMessage());
        }
    }
    public Boolean winCondition(){
        boolean win=false;
        //simplified if condition with IntelliJ
        win = (playerRow == endRow) && (playerColumn == endColumn);
        return win;
    }
    public ArrayList<String> keysList(String key){
        keyList.add(key);
        //System.out.println("The Player has these keys : "+ keyList);
        return keyList;
    }
    public String displayKeyList(){
        return "> You currently have these keys : "+keyList;
    }
    private static String keyCodeFinder(String colorCode) {
        //An enhanced switch case suggested by IntelliJ
        return switch (colorCode) {
            case "\033[31m\u2555\033[m" -> "Red";
            case "\033[32m\u2555\033[m" -> "Green";
            case "\033[33m\u2555\033[m" -> "Yellow";
            case "\033[34m\u2555\033[m" -> "Blue";
            case "\033[35m\u2555\033[m" -> "Magenta";
            case "\033[36m\u2555\033[m" -> "Cyan";
            default -> null;
        };
    }
    private static String doorCodeFinder(String doorColor){
        //if(doorType.equals("HD"))
        switch (doorColor) {
            case "\033[31m\u2592\033[m" -> doorColor = "Red";
            case "\033[32m\u2592\033[m" -> doorColor = "Green";
            case "\033[33m\u2592\033[m" -> doorColor = "Yellow";
            case "\033[34m\u2592\033[m" -> doorColor = "Blue";
            case "\033[35m\u2592\033[m" -> doorColor = "Magenta";
            case "\033[36m\u2592\033[m" -> doorColor = "Cyan";
        }
        return doorColor;
    }

}
