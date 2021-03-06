package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A journal in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Journal extends Entity implements Venue {
    private final String name;
    private final String publisher;
    private ArrayList<Publication> publications;
    
    /**
     * Creates new journal with provided arguments.
     * 
     * @param name the journal's name
     * @param publisher the journal's publisher
     */
    public Journal(String name, String publisher) {
        super();
        this.name = name;
        this.publisher = publisher;
        publications = new ArrayList<Publication>();
    }

    @Override
    public void addKeywords(Collection<String> words) {
        getKeywords().addAll(words);
        for (Publication pub : publications) {
            pub.addKeywords(words);
        }
    }
    
    @Override
    public void addArticle(Article newArticle) {
        publications.add(newArticle);
    }
    
    @Override
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
    
    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Journal) && name.equals(((Journal) other).getName());
    } 
}
