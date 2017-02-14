package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * A conference in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Conference {
    private final int year;
    private final String location;
    private ArrayList<Article> publications;
    
    /**
     * Creates new conference with provided arguments.
     * 
     * @param year the year
     * @param location the location
     */
    public Conference(int year, String location) {
        this.year = year;
        this.location = location;
    }
    
    
}
