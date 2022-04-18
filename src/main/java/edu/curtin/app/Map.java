package edu.curtin.app;

import java.util.logging.Logger;

public class Map {
    public Cell[][] map;
    private int playerRow, playerColumn, endColumn, endRow,actualRows,actualCols;
    private static Logger logger = Logger.getLogger(Map.class.getName());

    public final static char North = 'n';

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
   // @Override
    public String display(){
        System.out.println("map length is : "+map.length+"\n");
        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < map.length; i++) {
//            builder.append(" _ ");
//        }
//        builder.append("\n");

        for (int i = 0; i < actualRows; i++) {
            for (int j = 0; j < actualCols; j++) {
                if(map[i][j]==null){
                    builder.append(" ");
                }else{
                    builder.append("").append(map[i][j]).append("");
                }
            }
            builder.append("\n");
        }
//        for (int i = 0; i < map.length; i++) {
//            builder.append(" _ ");
//        }
//        builder.append("\n");
        return builder.toString();
    }
    public void move(char input){
        switch (input){
            case 'n' :
                if(playerRow==0){
                    System.out.println("Player has moved outside map");
                }
                if(map[playerRow-2][playerColumn] instanceof VerticalWall){
                    System.out.println("Player has hit a wall");
                }
                map[playerRow-2][playerColumn] = map[playerRow][playerColumn];
                map[playerRow][playerColumn] = null;
                logger.info(String.format("Player moved from (%d,%d) to (%d,%d)", playerRow, playerColumn, playerRow-1, playerColumn));
                playerRow = playerRow-2;
                break;
            case 's' :
                if(playerRow==0){
                    System.out.println("Player has moved outside map");
                }
                if(map[playerRow+2][playerColumn] instanceof VerticalWall){
                    System.out.println("Player has hit a wall");
                }
                break;
            default:
                System.err.println("Invalid move");

        }
    }
}
