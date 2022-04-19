package edu.curtin.app.Model.Doors.HorizontalDoors;

import edu.curtin.app.Model.Doors.HorizontalDoor;

public class YellowHD extends HorizontalDoor {
    @Override
    public String toString(){
        Object door = "\033[33m\u2592\033[m";
        return door.toString();
    }
}
