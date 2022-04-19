package edu.curtin.app.Model;

import edu.curtin.app.Exceptions.HitHorizontalWall;
import edu.curtin.app.Exceptions.HitVerticalWall;
import edu.curtin.app.Exceptions.MazeException;
import edu.curtin.app.Model.Borders.botIntersection;
import edu.curtin.app.Model.Borders.leftIntersection;
import edu.curtin.app.Model.Borders.rightIntersection;
import edu.curtin.app.Model.Borders.topIntersection;
import edu.curtin.app.Model.Keys.Keys;
import edu.curtin.app.Model.Walls.HorizontalWall;
import edu.curtin.app.Model.Walls.VerticalWall;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Map {
    public Cell[][] map;
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
                    }else if(map[playerRow-1][playerColumn] instanceof HorizontalWall){
                        throw new HitHorizontalWall("You bumped into a Horizontal Wall next to you!");
                    }else if(map[playerRow-2][playerColumn] instanceof Keys){
                        System.out.println("You have obtained a key");
                        logger.info("Player has obtained a key at ["+(playerRow-2)+","+playerColumn+"]");
                    }
                    map[playerRow-2][playerColumn] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved NORTH from ["+playerRow+","+playerColumn+"] to ["+(playerRow-2)+","+playerColumn+"]");
                    playerRow = playerRow-2;
                    break;
                case "s" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                    } else if(map[playerRow+1][playerColumn] instanceof HorizontalWall){
                        throw new HitHorizontalWall("You bumped into a Horizontal Wall next to you!");
                    } else if(map[playerRow+2][playerColumn] instanceof Keys){
                        System.out.println("You have obtained a key");
                        logger.info("Player has obtained a key at ["+(playerRow+2)+","+playerColumn+"]");

                    }
                    map[playerRow+2][playerColumn] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved SOUTH from ["+playerRow+","+playerColumn+"] to ["+(playerRow+2)+","+playerColumn+"]");
                    playerRow = playerRow+2;
                    break;
                case "w" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                    } else if(map[playerRow][playerColumn-2] instanceof VerticalWall){
                        throw new HitVerticalWall("You bumped into a Vertical Wall next to you!");
                    } else if(map[playerRow][playerColumn-4] instanceof Keys){
                        System.out.println("You have obtained a key");
                        logger.info("Player has obtained a key at ["+playerRow+","+(playerColumn-4)+"]");

                    }
                    map[playerRow][playerColumn-4] = map[playerRow][playerColumn];
                    map[playerRow][playerColumn] = null;
                    logger.info("Player has moved WEST from ["+playerRow+","+playerColumn+"] to ["+playerRow+","+(playerColumn-4)+"]");
                    playerColumn = playerColumn-4;
                    break;
                case "e" :
                    if(playerRow==0){
                        System.out.println("Player has moved outside map");
                    } else if(map[playerRow][playerColumn+2] instanceof VerticalWall){
                        throw new HitVerticalWall("You bumped into a Vertical Wall next to you!");
                    } else if(map[playerRow][playerColumn+4] instanceof Keys){
                        System.out.println("You have obtained a key");
                        logger.info("Player has obtained a key at ["+playerRow+","+(playerColumn+4)+"]");

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
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(key);
        return keyList;
    }


}
