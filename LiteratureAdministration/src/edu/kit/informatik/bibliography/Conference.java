package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A conference in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Conference extends Entity {
    private final int year;
    private final String location;
    private ArrayList<Publication> publications;
    
    /**
     * Creates new conference with provided arguments.
     * 
     * @param year the year
     * @param location the location
     */
    public Conference(int year, String location) {
        super();
        this.year = year;
        this.location = location;
        publications = new ArrayList<Publication>();
    }

    /**
     * Adds a new article to this conference's publications.
     * 
     * @param newArticle the article
     */
    public void addArticle(Article newArticle) {
        publications.add(newArticle);
    }
    
    @Override
    public void addKeywords(Collection<String> words) {
        getKeywords().addAll(words);
        for (Publication pub : publications) {
            pub.addKeywords(words);
        }
    }
    
    /**
     * Returns the year this conference held place.
     * 
     * @return the year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Returns the location of the conference.
     * 
     * @return the location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Returns the publications belonging to this conference.
     * 
     * @return the publications
     */
    public ArrayList<Publication> getPublications() {
        return publications;
    }
    
    @Override
    public String toString() {
        return location + ", " + year;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Conference) && (year == ((Conference) other).getYear());
    }
    
}
