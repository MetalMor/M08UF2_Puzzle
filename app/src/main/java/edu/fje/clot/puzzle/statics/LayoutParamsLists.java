package edu.fje.clot.puzzle.statics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m0r on 30/01/17.
 */

public class LayoutParamsLists {
    private static List<Integer> listX;
    private static List<Integer> listY;

    public static List<Integer> getListX() {
        if(listX == null) {
            listX = new ArrayList<Integer>();
            listX.add(5);
            listX.add(110);
            listX.add(215);
            listX.add(5);
            listX.add(110);
            listX.add(215);
            listX.add(5);
            listX.add(110);
            listX.add(215);
        }
        return listX;
    }

    public static List<Integer> getListY() {
        if(listY == null) {
            listY = new ArrayList<Integer>();
            listY.add(5);
            listY.add(5);
            listY.add(5);
            listY.add(110);
            listY.add(110);
            listY.add(110);
            listY.add(215);
            listY.add(215);
            listY.add(215);
        }
        return listY;
    }
}
