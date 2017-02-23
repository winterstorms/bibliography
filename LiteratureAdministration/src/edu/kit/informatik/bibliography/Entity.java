package edu.kit.informatik.bibliography;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Implements the  general type of an entity like specified in the task.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 17.02.2017
 */
public abstract class Entity {
    private TreeSet<String> keywords;
    
    /**
     * Creates new entity;
     */
    public Entity() {
        keywords = new TreeSet<String>();
    }
    /**
     * Adds a list of keywords to this entity.
     * 
     * @param words the keywords
     */
    public void addKeywords(Collection<String> words) {
        keywords.addAll(words);
    }
    
    /**
     * Checks whether this publication has all the provided keywords.
     * 
     * @param words the keywords
     * 
     * @return true if list of keywords includes all words 
     */
    public boolean hasKeywords(Collection<String> words) {
        return keywords.containsAll(words);
    }
    
    /**
     * Returns the publication's set of keywords.
     * 
     * @return the keywords
     */
    public TreeSet<String> getKeywords() {
        return keywords;
    }
    
    @Override
    public abstract String toString();
}
