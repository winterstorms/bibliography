package edu.kit.informatik.bibliography;

import java.util.Collection;

/**
 * Implements the  general type of an entity like specified in the task.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 17.02.2017
 */
public interface MyEntity {
    @Override
    String toString();
    
    /**
     * Adds a list of keywords to this entity.
     * 
     * @param words the keywords
     */
    void addKeywords(Collection<String> words);
}
