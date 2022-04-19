package edu.curtin.app.Model.Doors.HorizontalDoors;

import edu.curtin.app.Model.Doors.HorizontalDoor;

public class RedHD extends HorizontalDoor {
    @Override
    public String toString(){
        Object door = "\033[31m\u2592\033[m";
        return door.toString();
    }
}
