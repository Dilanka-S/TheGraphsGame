package edu.curtin.app.Model;

import edu.curtin.app.Model.Borders.botIntersection;
import edu.curtin.app.Model.Borders.leftIntersection;
import edu.curtin.app.Model.Borders.rightIntersection;
import edu.curtin.app.Model.Borders.topIntersection;
import edu.curtin.app.Model.Walls.HorizontalWall;
import edu.curtin.app.Model.Walls.VerticalWall;

import java.util.logging.Logger;

public class Map {
    public Cell[][] map;
    private int playerRow, playerColumn, endColumn, endRow,actualRows,actualCols;
    private static Logger logger = Logger.getLogger(Map.class.getName());

    //public final static char North = 'n';

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
   // @Override
    public String display(){
        //System.out.println("map length is : "+map.length+"\n");
        StringBuilder builder = new StringBuilder();
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
        return builder.toString();
    }
    public void move(String input){
        switch (input){
            case "n" :
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
            case "s" :
                if(playerRow==0){
                    System.out.println("Player has moved outside map");
                }
                if(map[playerRow+2][playerColumn] instanceof VerticalWall){
                    System.out.println("Player has hit a wall");
                }
                map[playerRow+2][playerColumn] = map[playerRow][playerColumn];
                map[playerRow][playerColumn] = null;
                logger.info(String.format("Player moved from (%d,%d) to (%d,%d)", playerRow, playerColumn, playerRow+1, playerColumn));
                playerRow = playerRow+2;
                break;
            case "e" :
                if(playerRow==0){
                    System.out.println("Player has moved outside map");
                }
                if(map[playerRow-4][playerColumn] instanceof VerticalWall){
                    System.out.println("Player has hit a wall");
                }
                map[playerRow][playerColumn-4] = map[playerRow][playerColumn];
                map[playerRow][playerColumn] = null;
                logger.info(String.format("Player moved from (%d,%d) to (%d,%d)", playerRow, playerColumn, playerRow, playerColumn-1));
                playerColumn = playerColumn-4;
                break;
            case "w" :
                if(playerRow==0){
                    System.out.println("Player has moved outside map");
                }
                if(map[playerRow+4][playerColumn] instanceof VerticalWall){
                    System.out.println("Player has hit a wall");
                }
                map[playerRow][playerColumn+4] = map[playerRow][playerColumn];
                map[playerRow][playerColumn] = null;
                logger.info(String.format("Player moved from (%d,%d) to (%d,%d)", playerRow, playerColumn, playerRow, playerColumn)+1);
                playerColumn = playerColumn+4;
                break;
            default:
                System.err.println("Invalid move");
                break;

        }
    }
    public Boolean winCondition(){
        boolean win=false;
        //simplified if condition with IntelliJ
        win = (playerRow == endRow) && (playerColumn == endColumn);
        return win;
    }

}
