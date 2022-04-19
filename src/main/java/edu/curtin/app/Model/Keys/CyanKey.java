package edu.curtin.app.Model.Keys;

import edu.curtin.app.Model.Cell;

public class CyanKey extends Keys implements Cell {
    @Override
    public String toString(){
        Object key = "\033[36m\u2555\033[m";
        return key.toString();
    }
}
