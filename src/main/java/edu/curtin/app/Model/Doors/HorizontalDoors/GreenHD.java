package edu.curtin.app.Model.Doors.HorizontalDoors;

import edu.curtin.app.Model.Doors.HorizontalDoor;

public class GreenHD extends HorizontalDoor {
    @Override
    public String toString(){
        Object door = "\033[32m\u2592\033[m";
        return door.toString();
    }
}
