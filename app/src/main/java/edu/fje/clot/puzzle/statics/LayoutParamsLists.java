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
            listX.add(10);
            listX.add(360);
            listX.add(710);
            listX.add(10);
            listX.add(360);
            listX.add(710);
            listX.add(10);
            listX.add(360);
            listX.add(710);
        }
        return listX;
    }

    public static List<Integer> getListY() {
        if(listY == null) {
            listY = new ArrayList<Integer>();
            listY.add(10);
            listY.add(10);
            listY.add(10);
            listY.add(360);
            listY.add(360);
            listY.add(360);
            listY.add(710);
            listY.add(710);
            listY.add(710);
        }
        return listY;
    }
}
