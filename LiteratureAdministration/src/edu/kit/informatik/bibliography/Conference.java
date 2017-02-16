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

    /**
     * Adds a new article to this conference's publications.
     * 
     * @param newArticle the article
     */
    public void addArticle(Article newArticle) {
        publications.add(newArticle);
    }
    
    /**
     * Adds the keywords to every article in this conference.
     * 
     * @param words the keywords
     */
    public void addKeywords(ArrayList<String> words) {
        for (Article pub : publications) {
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
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Conference) && (year == ((Conference) other).getYear());
    }
    
}
