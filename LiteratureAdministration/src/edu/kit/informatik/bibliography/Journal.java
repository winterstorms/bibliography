package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * A journal in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Journal {
    private final String name;
    private final String publisher;
    private ArrayList<Article> publications;
    
    /**
     * Creates new journal with provided arguments.
     * 
     * @param name the journal's name
     * @param publisher the journal's publisher
     */
    public Journal(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
    }
    
    /**
     * Returns the name of this journal.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the publisher of the journal.
     * 
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }
    
}
