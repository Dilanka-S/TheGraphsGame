package edu.curtin.app.Model.Doors.HorizontalDoors;

import edu.curtin.app.Model.Doors.HorizontalDoor;

public class BlueHD extends HorizontalDoor {
    @Override
    public String toString(){
        Object door = "\033[34m\u2592\033[m";
        return door.toString();
    }
}
