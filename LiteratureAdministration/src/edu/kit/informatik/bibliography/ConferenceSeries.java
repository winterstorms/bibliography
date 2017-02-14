package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * A series of conferences in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class ConferenceSeries {
    private final String name;
    private ArrayList<Conference> conferences;
    
    /**
     * Creates new series of conferences with provided argument.
     * 
     * @param name the name of the series
     */
    public ConferenceSeries(String name) {
        this.name = name;
    }
    
    /**
     * Adds conference to this series.
     * 
     * @param newConference the conference to add
     */
    public void addConference(Conference newConference) {
        conferences.add(newConference);
    }
    
    /**
     * Returns the name of the series.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
}
