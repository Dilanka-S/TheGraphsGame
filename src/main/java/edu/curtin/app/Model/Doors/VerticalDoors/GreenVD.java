package edu.curtin.app.Model.Doors.VerticalDoors;

import edu.curtin.app.Model.Doors.VerticalDoor;

public class GreenVD extends VerticalDoor {
    @Override
    public String toString(){
        Object door = "\033[32m\u2592\033[m";
        return door.toString();
    }
}
