package edu.curtin.app.Exceptions;

public class DoorAccessException extends MazeException{
    public DoorAccessException(){}
    public DoorAccessException(String doorAccess){
        super(doorAccess);
    }
}
