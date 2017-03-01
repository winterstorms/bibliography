package edu.kit.informatik.bibliography;

import java.util.TreeSet;

/**
 * Implements the venue for a publication.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 16.02.2017
 */
public interface Venue {
    /**
     * Adds the provided publication to the venue.
     * 
     * @param newArticle the publication
     */
    void addArticle(Article newArticle);
    
    /**
     * Returns the venue's name.
     * 
     * @return the name
     */
    String getName();
    
    /**
     * Returns the publication's set of keywords.
     * 
     * @return the keywords
     */
    TreeSet<String> getKeywords();
}
