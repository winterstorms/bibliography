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
     * Adds a new article to this journal's publications.
     * 
     * @param newArticle the article
     */
    public void addArticle(Article newArticle) {
        publications.add(newArticle);
    }
    
    /**
     * Adds the keywords to every article in this journal.
     * 
     * @param words the keywords
     */
    public void addKeywords(ArrayList<String> words) {
        for (Article pub : publications) {
            pub.addKeywords(words);
        }
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
