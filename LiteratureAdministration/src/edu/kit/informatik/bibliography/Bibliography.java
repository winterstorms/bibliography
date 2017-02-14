package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Bibliography {
    private ArrayList<Author> authors;
    private ArrayList<Journal> journals;
    private ArrayList<ConferenceSeries> series;
    private ArrayList<Article> publications;
    
    /**
     * Creates new bibliography with no content.
     */
    public Bibliography() { }
    
    /**
     * Adds a new author with the provided arguments to the list of authors in the bibliography.
     * 
     * @param firstname the forename
     * @param lastname the surname
     */
    public void addAuthor(String firstname, String lastname) {
        authors.add(new Author(firstname, lastname));
    }
    
    /**
     * Adds a new journal with the provided arguments to the list of journals in the bibliography.
     * 
     * @param name its name
     * @param publisher its publisher
     */
    public void addJournal(String name, String publisher) {
        journals.add(new Journal(name, publisher));
    }
    
    /**
     * Adds a new conference series with the provided name to the list of conference series' the bibliography.
     * 
     * @param name the name
     */
    public void addConferenceSeries(String name) {
        series.add(new ConferenceSeries(name));
    }
    
    /**
     * Adds a new conference to an already existing conference series in the bibliography.
     * 
     * @param series the conference series
     * @param location the location of the conference
     * @param year the year in which the conference takes place
     * 
     * @throws NoSuchElementException if no conference series with the provided name exists
     */
    public void addConference(String series, String location, int year) throws NoSuchElementException {
        
    }
}
