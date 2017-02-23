package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * A series of conferences in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Series extends Entity implements Venue {
    private final String name;
    private ArrayList<Conference> conferences;
    
    /**
     * Creates new series of conferences with provided argument.
     * 
     * @param name the name of the series
     */
    public Series(String name) {
        super();
        this.name = name;
        conferences = new ArrayList<Conference>();
    }
    
    /**
     * Adds conference to this series.
     * 
     * @param newConference the conference to add
     */
    public void addConference(Conference newConference) {
        conferences.add(newConference);
    }
    
    @Override
    public void addKeywords(Collection<String> words) {
        getKeywords().addAll(words);
        for (Conference conf : conferences) {
            conf.addKeywords(words);
        }
    }
    
    @Override
    public void addArticle(Article newArticle) throws NoSuchElementException {
        findConference(newArticle.getYear()).addArticle(newArticle);
    }
    
    /**
     * Returns the conference with the given unique id if it is in this series.
     * 
     * @param identifier the year in which the conference held place
     * 
     * @return the conference
     * @throws NoSuchElementException if the bibliography does not contain the series
     */
    public Conference findConference(int identifier) throws NoSuchElementException  {
        for (Conference c : getConferences()) {
            if (c.getYear() == identifier) return c;
        }
        throw new NoSuchElementException("conference not found.");
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Returns the ArrayList of conferences belonging to this series.
     * 
     * @return the ArrayList
     */
    public ArrayList<Conference> getConferences() {
        return conferences;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Series) && name.equals(((Series) other).getName());
    }
}
